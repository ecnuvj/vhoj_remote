package com.vjudge.ecnuvj.remote.provider.nbut;

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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NBUTQuerier extends AuthenticatedQuerier {

    @Override
    public RemoteOjInfo getOjInfo() {
        return NBUTInfo.INFO;
    }

    @Override
    protected SubmissionRemoteStatus query(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        SubmissionRemoteStatus status = new SubmissionRemoteStatus();
        String html = client.get("/Problem/viewcode.xhtml?submitid=" + info.remoteRunId, HttpStatusValidator.SC_OK).getBody();
        String regex = "当前状态: <span.*?>(.+?)</span>　运行时间: (\\d+?) ms　运行内存: (\\d+?) K";
        Matcher matcher = Pattern.compile(regex).matcher(html);
        Validate.isTrue(matcher.find());
        status.rawStatus = StringUtils.capitalize(matcher.group(1).replaceAll("<[^<>]*>", "").replace('_', ' ').trim().toLowerCase());
        status.statusType = SubstringNormalizer.DEFAULT.getStatusType(status.rawStatus);
        status.executionTime = Integer.parseInt(matcher.group(2));
        status.executionMemory = Integer.parseInt(matcher.group(3));
        if (status.statusType == RemoteStatusType.CE) {
            html = client.get("/Problem/viewce.xhtml?submitid=" + info.remoteRunId).getBody();
            status.compilationErrorInfo = Tools.regFind(html, "(<pre style=\"overflow-x: auto;\">[\\s\\S]*?</pre>)");
        }
        return status;
    }

}
