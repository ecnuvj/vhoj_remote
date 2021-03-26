package com.vjudge.ecnuvj.remote.provider.hust;

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
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HUSTQuerier extends AuthenticatedQuerier {

    @Override
    public RemoteOjInfo getOjInfo() {
        return HUSTInfo.INFO;
    }

    @Override
    protected SubmissionRemoteStatus query(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        String html = client.get("/solution/source/" + info.remoteRunId, HttpStatusValidator.SC_OK).getBody();

        String regex =
                "<span class=\"badge\">(.*?)</span>\\s*Result[\\s\\S]*?" +
                        "<span class=\"badge\">(\\d+)ms</span>\\s*Time[\\s\\S]*?" +
                        "<span class=\"badge\">(\\d+)kb</span>\\s*Memory";
        Matcher matcher = Pattern.compile(regex).matcher(html);
        Validate.isTrue(matcher.find());

        SubmissionRemoteStatus status = new SubmissionRemoteStatus();
        status.rawStatus = matcher.group(1).trim();
        status.executionTime = Integer.parseInt(matcher.group(2));
        status.executionMemory = Integer.parseInt(matcher.group(3));
        status.statusType = SubstringNormalizer.DEFAULT.getStatusType(status.rawStatus);
        if (status.statusType == RemoteStatusType.CE) {
            status.compilationErrorInfo = (Tools.regFind(html, "(<pre class=\"col-sm-12 linenums\">[\\s\\S]*?</pre>)"));
        }
        return status;
    }

}
