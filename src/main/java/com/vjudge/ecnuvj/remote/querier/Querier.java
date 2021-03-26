package com.vjudge.ecnuvj.remote.querier;

import com.vjudge.ecnuvj.remote.common.RemoteOjAware;
import com.vjudge.ecnuvj.remote.status.SubmissionRemoteStatus;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import com.vjudge.ecnuvj.tool.Handler;

/**
 * Short for "Submission remote status querier"
 * Implementation should be stateless.
 *
 * @author Isun
 */
public interface Querier extends RemoteOjAware {

    void query(SubmissionInfo info, Handler<SubmissionRemoteStatus> handler);

}
