package com.groot.flow.processor;



import com.alibaba.fastjson.JSON;
import com.groot.flow.constant.Constants;
import com.groot.flow.constant.GrootRequestCode;
import com.groot.flow.constant.GrootResponseCode;
import com.groot.flow.constant.GrootResult;
import com.groot.flow.exception.NoAvailableWorkThreadException;
import com.groot.flow.exception.RemotingTimeoutException;
import com.groot.flow.job.JobMetaData;
import com.groot.flow.job.context.JobExecuteApplicationContext;
import com.groot.flow.job.runner.ExecuteCallback;
import com.groot.flow.remoting.GrootRemotingClientDelegate;
import com.groot.flow.remoting.GrootReponseCode;
import com.groot.flow.remoting.command.*;
import com.groot.flow.remoting.channel.GrootChannel;
import com.groot.flow.utils.CollectionUtils;
import com.groot.flow.utils.SystemClock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author : chenhaitao934
 * @date : 9:01 下午 2020/5/22
 */
public class JobHandlerProcessor extends AbstractProcessor implements GrootProcessor{

    private JobExecuteCallback jobExecuteCallback;

    private GrootRemotingClientDelegate remotingClientDelegate;

    public JobHandlerProcessor(JobExecuteApplicationContext context) {
        super(context);
        this.jobExecuteCallback = new JobExecuteCallback();
        this.remotingClientDelegate = context.getRemotingClientDelegate();
    }

    @Override
    public GrootCommand processRequest(GrootChannel channel, GrootCommand request) throws Exception {
        JobPushRequest body = request.getBody();
        List<JobMetaData> jobMetaDataList = body.getJobMetaDataList();
        List<String> failureJobId = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(jobMetaDataList)){
            jobMetaDataList.forEach((jobMetaData) -> {
                JobExecuteApplicationContext context = (JobExecuteApplicationContext) this.context;
                try {
                    context.getJobRunnerPool().execute(jobMetaData, new JobExecuteCallback(), context);
                } catch (NoAvailableWorkThreadException e) {
                    failureJobId.add(jobMetaData.getJobId());
                }
            });
        }
        if(CollectionUtils.isNotEmpty(failureJobId)){
            JobRunResponse response = new JobRunResponse();
            response.setFailureJobId(failureJobId);
            return GrootCommand.createResponseCommand(GrootResponseCode.NO_AVAILABLE_WORK_THREAD.getCode(),
                    "no available work thread", response);
        }
        return  GrootCommand.createResponseCommand(GrootResponseCode.JOB_RUN_SUCCESS.getCode(), "job run success");
    }

    private class JobExecuteCallback implements ExecuteCallback {

        @Override
        public JobMetaData complete(GrootResult result) throws RemotingTimeoutException {
            result.setCompleteTime(SystemClock.now());
            GrootCompleteRequest completeRequest = context.getWrapper().wrapper(new GrootCompleteRequest());
            completeRequest.addGrootResults(result);
            completeRequest.setReceiveNewTask(result.isReceiveNewTask());
            int requestCode = GrootRequestCode.JOB_COMPLETED.getCode();
            GrootCommand requestCommand = GrootCommand.createRequestCommand(requestCode, completeRequest);

            GrootResult grootResult = new GrootResult();
            final CountDownLatch latch = new CountDownLatch(1);
            try {
                remotingClientDelegate.invokeAsync(requestCommand, responseFuture -> {
                    GrootCommand responseCommand = responseFuture.getResponseCommand();
                    if(responseCommand != null && responseCommand.getCode() == GrootReponseCode.SUCCESS.code()){
                        JobPushRequest jobPushRequest = responseCommand.getBody();
                        if(jobPushRequest !=null){
                            if(CollectionUtils.isNotEmpty(jobPushRequest.getJobMetaDataList())){
                                grootResult.setJobMetaData(jobPushRequest.getJobMetaDataList().get(0));
                            }
                        }
                    }else {
                        logger.error("send job result to server error, code = {}, reust={}",
                                responseCommand != null ? responseCommand.getCode() : null, JSON.toJSONString(result));
                        // TODO 将发送失败的Job存储起来，供重新发送使用

                    }
                });
            } finally {
                latch.countDown();
            }
            try {
                latch.await(Constants.LATCH_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RemotingTimeoutException(e.getMessage());
            }
            return grootResult.getJobMetaData();
        }
    }
}
