package com.vjudge.ecnuvj.remote.manager;

import com.vjudge.ecnuvj.bean.Submission;
import com.vjudge.ecnuvj.executor.ExecutorTaskType;
import com.vjudge.ecnuvj.executor.Task;
import com.vjudge.ecnuvj.remote.common.RemoteStatusUpdateEvent;
import com.vjudge.ecnuvj.remote.common.RunningSubmissions;
import com.vjudge.ecnuvj.remote.common.SubmissionConverter;
import com.vjudge.ecnuvj.remote.querier.Querier;
import com.vjudge.ecnuvj.remote.querier.QueriersHolder;
import com.vjudge.ecnuvj.remote.status.RemoteStatusType;
import com.vjudge.ecnuvj.remote.status.SubmissionRemoteStatus;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import com.vjudge.ecnuvj.service.SubmissionService;
import com.vjudge.ecnuvj.tool.Handler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class QueryStatusManager {
    private final static Logger log = LoggerFactory.getLogger(QueryStatusManager.class);

    /**
     * If a submission keeps remote status unchanged for more than this
     * length(in milliseconds), and is still with intermediate status, stop
     * querying it.
     */
    private final static long MAX_PATIENCE_LENGTH = 60000L;

    /**
     * If s submission is queried failed and was submitted such milliseconds
     * ago, regard it as a dead submission and allow resubmit.
     */
    private final static long MAX_CONFIDENCE_LENGTH = 3 * 86400L * 1000L;

    @Autowired
    protected RunningSubmissions runningSubmissions;

    @Autowired
    private RemoteStatusUpdateEvent updateEvent;

    @Autowired
    private SubmissionService submissionService;

    // 查询管理器的调用必然基于用户的初步提交
    public void createQuery(final Submission submission) {
        // 任务不存在时、任务繁忙时均不执行
        if (submission.getId() == 0) {
            log.error(String.format("Please persist first: %s", runningSubmissions.getLogKey(submission)));
            return;
        }
        if (StringUtils.isBlank(submission.getRealRunId())) {
            log.error(String.format("Please submit first: %s", runningSubmissions.getLogKey(submission)));
            return;
        }
        if (!runningSubmissions.contains(submission.getId())) {
            log.info(String.format("Create query: %s", runningSubmissions.getLogKey(submission)));
            runningSubmissions.add(submission);
            new QueryStatusTask(submission, 0).submit();
        }
    }

    class QueryStatusTask extends Task<Void> {
        private Submission submission;

        public QueryStatusTask(Submission submission, int delaySeconds) {
            super(ExecutorTaskType.QUERY_SUBMISSION_STATUS, delaySeconds);
            this.submission = submission;
        }

        @Override
        public Void call() throws Exception {
            try {
                query(submission);
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
                stopQuery(submission);
            }
            return null;
        }

        private void query(final Submission submission) {
            if (!runningSubmissions.contains(submission.getId())) {
                return;
            }

            final SubmissionInfo info = SubmissionConverter.toInfo(submission);
            Querier querier = QueriersHolder.getQuerier(info.remoteOj);
            querier.query(info, new Handler<SubmissionRemoteStatus>() {
                @Override
                public void handle(SubmissionRemoteStatus remoteStatus) {
                    try {
                        _handle(remoteStatus);
                    } catch (Throwable t) {
                        log.error(t.getMessage(), t);
                        stopQuery(submission);
                    }
                }

                private void _handle(SubmissionRemoteStatus remoteStatus) {
                    if (remoteStatus.statusType == RemoteStatusType.SUBMIT_FAILED_TEMP) {
                        submission.reset();
                        stopQuery(submission);
                        return;
                    }

                    String originalRawStatus = submission.getStatus();
                    submission.setStatusCanonical(remoteStatus.statusType.name());
                    submission.setStatus(StringUtils.capitalize(remoteStatus.rawStatus));
                    submission.setTime(remoteStatus.executionTime);
                    submission.setMemory(remoteStatus.executionMemory);
                    submission.setAdditionalInfo(StringUtils.left(remoteStatus.compilationErrorInfo, 10000));
                    submission.setFailCase(remoteStatus.failCase);
                    submission.setQueryCount(submission.getQueryCount() + 1);
                    if (!StringUtils.equals(originalRawStatus, remoteStatus.rawStatus)) {
                        submission.setStatusUpdateTime(new Date());
                    }
                    log.info(String.format("Query(%d): %s", submission.getQueryCount(), runningSubmissions.getLogKey(submission)));

                    if ( //
                            remoteStatus.statusType.finalized || //
                                    submission.getStatusUpdateTime() != null && //
                                            remoteStatus.queryTime.getTime() - submission.getStatusUpdateTime().getTime() > MAX_PATIENCE_LENGTH) {
                        stopQuery(submission);
                        return;
                    }

                    int nextQueryDelaySeconds = submission.getQueryCount() + 2;
                    new QueryStatusTask(submission, nextQueryDelaySeconds).submit();
                }

                @Override
                public void onError(Throwable t) {
                    log.error(t.getMessage(), t);
                    if (submission.getSubTime() == null || System.currentTimeMillis() - submission.getRemoteSubmitTime().getTime() > MAX_CONFIDENCE_LENGTH) {
                        submission.reset();
                    }
                    stopQuery(submission);
                }
            });
        }

        private void stopQuery(Submission submission) {
            log.info(String.format("Stop query: %s", runningSubmissions.getLogKey(submission)));
            if (runningSubmissions.remove(submission.getId()) != null) {
                //baseService.addOrModify(submission);
                submissionService.addOrModifySubmission(submission);
                System.out.println(submission.getStatus());
            }
            updateEvent.fireStatusUpdate(submission);
        }

    }

}
