package com.vjudge.ecnuvj.remote.provider.sgu;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.querier.SyncQuerier;
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
public class SGUQuerier extends SyncQuerier {

    @Override
    public RemoteOjInfo getOjInfo() {
        return SGUInfo.INFO;
    }

    @Override
    protected SubmissionRemoteStatus query(SubmissionInfo info) {
        DedicatedHttpClient client = dedicatedHttpClientFactory.build(getOjInfo().mainHost, null, getOjInfo().defaultChaset);

        String html = client.get("/status.php?start=" + info.remoteRunId).getBody();
        Pattern pattern = Pattern.compile("<TD>" + info.remoteRunId + "</TD>[\\s\\S]*?<TD class=btab>([\\s\\S]*?)</TD>[\\s\\S]*?([\\d]*?) ms</TD><TD>([\\d]*?) kb</TD>");
        Matcher matcher = pattern.matcher(html);
        Validate.isTrue(matcher.find());

        SubmissionRemoteStatus status = new SubmissionRemoteStatus();
        status.rawStatus = matcher.group(1).replaceAll("<[\\s\\S]*?>", "").trim();
        status.statusType = SubstringNormalizer.DEFAULT.getStatusType(status.rawStatus);

        if (status.statusType == RemoteStatusType.AC) {
            status.executionMemory = Integer.parseInt(matcher.group(3));
            status.executionTime = Integer.parseInt(matcher.group(2));
        } else if (status.statusType == RemoteStatusType.CE) {
            html = client.get("/cerror.php?id=" + info.remoteRunId).getBody();
            status.compilationErrorInfo = Tools.regFind(html, info.remoteRunId + "</TD><TD>(<pre>[\\s\\S]*?</pre>)");
        }

        matcher = Pattern.compile("on test (\\d+)").matcher(status.rawStatus);
        if (matcher.find()) {
            status.failCase = Integer.parseInt(matcher.group(1));
        }

        return status;
    }

}
