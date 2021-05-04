package com.vjudge.ecnuvj.remote.manager;

import com.vjudge.ecnuvj.bean.Submission;
import com.vjudge.ecnuvj.executor.ExecutorTaskType;
import com.vjudge.ecnuvj.executor.Task;
import com.vjudge.ecnuvj.remote.common.RunningSubmissions;
import com.vjudge.ecnuvj.remote.common.SubmissionConverter;
import com.vjudge.ecnuvj.remote.status.RemoteStatusType;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import com.vjudge.ecnuvj.remote.submitter.SubmissionReceipt;
import com.vjudge.ecnuvj.remote.submitter.Submitter;
import com.vjudge.ecnuvj.remote.submitter.SubmittersHolder;
import com.vjudge.ecnuvj.service.SubmissionService;
import com.vjudge.ecnuvj.tool.Handler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubmitCodeManager {
    private final static Logger log = LoggerFactory.getLogger(SubmitCodeManager.class);

    @Autowired
    private QueryStatusManager queryStatusManager;

    @Autowired
    private RunningSubmissions runningSubmissions;

    @Autowired
    private SubmissionService submissionService;


    public void submitCode(Submission submission) {
        if (submission.getId() == 0) {
            log.error(String.format("Please persist first: %s", runningSubmissions.getLogKey(submission)));
            return;
        }
        if (!runningSubmissions.contains(submission.getId())) {
            log.info("Create submit: " + runningSubmissions.getLogKey(submission) + " [" + submission.getUsername() + "]");
            runningSubmissions.add(submission);
            new SubmitCodeTask(submission).submit();
        }
    }

    class SubmitCodeTask extends Task<Void> {
        private Submission submission;

        public SubmitCodeTask(Submission submission) {
            super(ExecutorTaskType.SUBMIT_CODE);
            this.submission = submission;
        }

        @Override
        public Void call() throws Exception {
            try {
                submitCode();
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
                onErrorNotSubmitted();
            }
            return null;
        }

        private void submitCode() throws Exception {
            final SubmissionInfo info = SubmissionConverter.toInfo(submission);// 格式转换
            Submitter submitter = SubmittersHolder.getSubmitter(info.remoteOj);// 获取三方实例
            submitter.submitCode(info, new Handler<SubmissionReceipt>() {// 异步线程
                @Override
                public void handle(SubmissionReceipt receipt) {
                    try {
                        _handle(receipt);
                    } catch (Throwable t) {
                        log.error(t.getMessage(), t);
                        runningSubmissions.remove(submission.getId());
                    }
                }

                @Override
                public void onError(Throwable t) {
                    log.error(t.getMessage(), t);
                    onErrorNotSubmitted();
                }

                private void _handle(SubmissionReceipt receipt) {
                    submission.setRealRunId(receipt.remoteRunId);
                    submission.setRemoteAccountId(receipt.remoteAccountId);
                    submission.setRemoteSubmitTime(receipt.submitTime);
                    if (receipt.remoteRunId == null) {
                        submission.setStatusCanonical(RemoteStatusType.SUBMIT_FAILED_PERM.name());
                        if (StringUtils.isBlank(receipt.errorStatus)) {
                            submission.setStatus("Submit Failed");
                        } else {
                            submission.setStatus(receipt.errorStatus);
                        }
                        log.error("Submit Failed: " + runningSubmissions.getLogKey(submission));
                    } else {
                        submission.setStatus("Submitted");
                        submission.setStatusCanonical(RemoteStatusType.SUBMITTED.name());
                        log.info("Submit Finished: " + runningSubmissions.getLogKey(submission));
                    }
                    //baseService.addOrModify(submission);
                    submissionService.addOrModifySubmission(submission);
                    System.out.println(submission.getStatus());
                    runningSubmissions.remove(submission.getId());

                    if (receipt.remoteRunId != null) {
                        queryStatusManager.createQuery(submission);
                    }
                }
            });
        }

        private void onErrorNotSubmitted() {
            runningSubmissions.remove(submission.getId());
            submission.setStatus("Submit Failed");
            submission.setStatusCanonical(RemoteStatusType.SUBMIT_FAILED_TEMP.name());
            //baseService.addOrModify(submission);
            submissionService.addOrModifySubmission(submission);
        }
    }

}
