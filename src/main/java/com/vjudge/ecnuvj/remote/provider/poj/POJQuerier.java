package com.vjudge.ecnuvj.remote.provider.poj;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpBodyValidator;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.querier.AuthenticatedQuerier;
import com.vjudge.ecnuvj.remote.status.RemoteStatusType;
import com.vjudge.ecnuvj.remote.status.SubmissionRemoteStatus;
import com.vjudge.ecnuvj.remote.status.SubstringNormalizer;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import com.vjudge.ecnuvj.tool.Tools;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

@Component
public class POJQuerier extends AuthenticatedQuerier {

    @Override
    public RemoteOjInfo getOjInfo() {
        return POJInfo.INFO;
    }

    @Override
    protected SubmissionRemoteStatus query(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        String html = client.get(
                "/showsource?solution_id=" + info.remoteRunId,
                new HttpBodyValidator("<title>Error</title>", true)).getBody();

        SubmissionRemoteStatus status = new SubmissionRemoteStatus();
        status.rawStatus = Tools.regFind(html, "<b>Result:</b>(.+?)</td>").replaceAll("<.*?>", "").trim();
        status.statusType = SubstringNormalizer.DEFAULT.getStatusType(status.rawStatus);
        if (status.statusType == RemoteStatusType.AC) {
            status.executionMemory = Integer.parseInt(Tools.regFind(html, "<b>Memory:</b> ([-\\d]+)"));
            status.executionTime = Integer.parseInt(Tools.regFind(html, "<b>Time:</b> ([-\\d]+)"));
        } else if (status.statusType == RemoteStatusType.CE) {
            html = client.get("/showcompileinfo?solution_id=" + info.remoteRunId).getBody();
            Validate.isTrue(html.contains("Compile Error"));
            status.compilationErrorInfo = Tools.regFind(html, "(<pre>[\\s\\S]*?</pre>)");
        }
        return status;
    }

}
