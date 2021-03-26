package com.vjudge.ecnuvj.remote.provider.tkoj;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpStatusValidator;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.querier.AuthenticatedQuerier;
import com.vjudge.ecnuvj.remote.status.RemoteStatusType;
import com.vjudge.ecnuvj.remote.status.SubmissionRemoteStatus;
import com.vjudge.ecnuvj.remote.status.SubstringNormalizer;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import com.vjudge.ecnuvj.tool.Tools;
import org.springframework.stereotype.Component;

/**
 * Created with IDEA
 * User: zzzz76
 * Date: 2020-12-12
 */
@Component
public class TKOJQuerier extends AuthenticatedQuerier {

    private static final String[] statusArray = new String[]{
            "Pending", "Pending Rejudging", "Compiling", "Running Judging", "Accepted", "Presentation Error", "Wrong Answer", "Time Limit Exceed", "Memory Limit Exceed", "Output Limit Exceed", "Runtime Error", "Compile Error", "Compile OK", "Runtime Finish"
    };

    @Override
    public RemoteOjInfo getOjInfo() {
        return TKOJInfo.INFO;
    }

    @Override
    protected SubmissionRemoteStatus query(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) throws Exception {
        String result = client.get("/status-ajax.php?solution_id=" + info.remoteRunId, HttpStatusValidator.SC_OK).getBody();
        String[] results = result.split(",");

        SubmissionRemoteStatus status = new SubmissionRemoteStatus();
        status.rawStatus = statusArray[Integer.parseInt(results[0])];
        status.statusType = SubstringNormalizer.DEFAULT.getStatusType(status.rawStatus);
        status.executionMemory = Integer.parseInt(results[1]);
        status.executionTime = Integer.parseInt(results[2]);

        if (status.statusType == RemoteStatusType.CE) {
            String ceinfo = client.get("/ceinfo.php?sid=" + info.remoteRunId).getBody();
            status.compilationErrorInfo = "<pre>" + Tools.regFind(ceinfo, "id='errtxt'\\s*?>([\\s\\S]*?</pre>)");
        }
        return status;
    }
}
