package com.groot.flow.processor;



import com.groot.flow.GrootContext;
import com.groot.flow.constant.GrootResponseCode;
import com.groot.flow.exception.NoAvailableWorkThreadException;
import com.groot.flow.job.JobMetaData;
import com.groot.flow.job.context.JobExecuteApplicationContext;
import com.groot.flow.job.runner.ExecuteCallback;
import com.groot.flow.remoting.JobPushRequest;
import com.groot.flow.remoting.GrootCommand;
import com.groot.flow.remoting.JobRunResponse;
import com.groot.flow.remoting.channel.GrootChannel;
import com.groot.flow.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : chenhaitao934
 * @date : 9:01 下午 2020/5/22
 */
public class JobHandlerProcessor extends AbstractProcessor implements GrootProcessor{

    private JobExecuteCallback jobExecuteCallback;

    public JobHandlerProcessor(GrootContext context) {
        super(context);
        this.jobExecuteCallback = new JobExecuteCallback();
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
                    context.getJobRunnerPool().execute(jobMetaData, new JobExecuteCallback());
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
        public JobMetaData complete() {
            // TODO
            return null;
        }
    }
}
