package com.groot.flow.processor;



import com.groot.flow.constant.GrootRequestCode;
import com.groot.flow.constant.GrootResponseCode;
import com.groot.flow.constant.GrootResult;
import com.groot.flow.exception.NoAvailableWorkThreadException;
import com.groot.flow.job.JobMetaData;
import com.groot.flow.job.context.JobExecuteApplicationContext;
import com.groot.flow.job.runner.ExecuteCallback;
import com.groot.flow.remoting.GrootRemotingClientDelegate;
import com.groot.flow.remoting.command.GrootCompleteRequest;
import com.groot.flow.remoting.command.JobPushRequest;
import com.groot.flow.remoting.command.GrootCommand;
import com.groot.flow.remoting.command.JobRunResponse;
import com.groot.flow.remoting.channel.GrootChannel;
import com.groot.flow.utils.CollectionUtils;
import com.groot.flow.utils.SystemClock;

import java.util.ArrayList;
import java.util.List;

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
        JobPushRequest body = (JobPushRequest) request.getBody();
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
        public JobMetaData complete(GrootResult result) {
            result.setCompleteTime(SystemClock.now());
            GrootCompleteRequest completeRequest = context.getWrapper().wrapper(new GrootCompleteRequest());
            completeRequest.addGrootResults(result);
            completeRequest.setReceiveNewTask(result.isReceiveNewTask());
            int requestCode = GrootRequestCode.JOB_COMPLETED.getCode();
            GrootCommand requestCommand = GrootCommand.createRequestCommand(requestCode, completeRequest);
            remotingClientDelegate.invokeSync(requestCommand);
            // TODO
            return null;
        }
    }
}
