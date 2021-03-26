package com.vjudge.ecnuvj.remote.querier;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClientFactory;
import com.vjudge.ecnuvj.remote.status.SubmissionRemoteStatus;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import com.vjudge.ecnuvj.tool.Handler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * No dependence on any other resource, just do it in invoking thread.
 *
 * @author Isun
 */
public abstract class SyncQuerier implements Querier {

    @Autowired
    protected DedicatedHttpClientFactory dedicatedHttpClientFactory;

    @Override
    public void query(SubmissionInfo info, Handler<SubmissionRemoteStatus> handler) {
        SubmissionRemoteStatus status = null;
        try {
            status = query(info);
        } catch (Throwable t) {
            handler.onError(t);
            return;
        }
        handler.handle(status);
    }

    abstract protected SubmissionRemoteStatus query(SubmissionInfo info) throws Exception;

}
