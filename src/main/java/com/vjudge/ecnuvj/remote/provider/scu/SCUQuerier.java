package com.vjudge.ecnuvj.remote.provider.scu;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SCUQuerier extends AuthenticatedQuerier {

    @Override
    public RemoteOjInfo getOjInfo() {
        return SCUInfo.INFO;
    }

    @Override
    protected SubmissionRemoteStatus query(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        String html = client.get("/soj/solutions.action?from=" + info.remoteRunId).getBody();

        Pattern pattern = Pattern.compile(
                "<td height=\"44\">" + info.remoteRunId + "</td>\\s*" +
                        "<td>.*?</td>\\s*" +
                        "<td>.*?</td>\\s*" +
                        "<td>.*?</td>\\s*" +
                        "<td>[\\s\\S]*?</td>\\s*" +
                        "<td>([\\s\\S]*?)</td>\\s*" +
                        "<td>(\\d+)</td>\\s*" +
                        "<td>(\\d+)</td>");
        Matcher matcher = pattern.matcher(html);
        Validate.isTrue(matcher.find());

        SubmissionRemoteStatus status = new SubmissionRemoteStatus();
        status.rawStatus = matcher.group(1).replace("<BR>", " ").replaceAll("<.*?>", "").trim();
        status.statusType = SubstringNormalizer.DEFAULT.getStatusType(status.rawStatus);
        if (status.statusType == RemoteStatusType.AC) {
            status.executionTime = Integer.parseInt(matcher.group(2));
            status.executionMemory = Integer.parseInt(matcher.group(3));
        } else if (status.statusType == RemoteStatusType.CE) {
            html = client.get("/soj/judge_message.action?id=" + info.remoteRunId).getBody();
            status.compilationErrorInfo = Tools.regFind(html, "(<pre>[\\s\\S]*?</pre>)");
        }

        return status;
    }

}
