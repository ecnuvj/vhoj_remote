package com.vjudge.ecnuvj.remote.querier;

import com.vjudge.ecnuvj.executor.ExecutorTaskType;
import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.DedicatedHttpClientFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.account.RemoteAccountTask;
import com.vjudge.ecnuvj.remote.loginer.LoginersHolder;
import com.vjudge.ecnuvj.remote.status.SubmissionRemoteStatus;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import com.vjudge.ecnuvj.tool.Handler;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Which need to login to query (and of course, not SyncQuerier)
 *
 * @author Isun
 */
public abstract class AuthenticatedQuerier implements Querier {

    @Autowired
    private DedicatedHttpClientFactory dedicatedHttpClientFactory;

    @Override
    public void query(SubmissionInfo info, Handler<SubmissionRemoteStatus> handler) {
        new QueryTask(info, handler).submit();
    }

    class QueryTask extends RemoteAccountTask<SubmissionRemoteStatus> {
        SubmissionInfo info;

        public QueryTask(SubmissionInfo info, Handler<SubmissionRemoteStatus> handler) {
            super(
                    ExecutorTaskType.QUERY_SUBMISSION_STATUS,
                    getOjInfo().remoteOj,
                    info.remoteAccountId,
                    null,
                    handler);
            this.info = info;
        }

        @Override
        protected SubmissionRemoteStatus call(RemoteAccount remoteAccount) throws Exception {
            LoginersHolder.getLoginer(getOjInfo().remoteOj).login(remoteAccount);
            DedicatedHttpClient client = dedicatedHttpClientFactory.build(getHost(), remoteAccount.getContext(), getCharset());
            return query(info, remoteAccount, client);
        }
    }

    protected abstract SubmissionRemoteStatus query(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) throws Exception;

    /**
     * Can be overridden
     *
     * @return
     */
    protected HttpHost getHost() {
        return getOjInfo().mainHost;
    }

    /**
     * Can be overridden
     *
     * @return
     */
    protected String getCharset() {
        return getOjInfo().defaultChaset;
    }

}
