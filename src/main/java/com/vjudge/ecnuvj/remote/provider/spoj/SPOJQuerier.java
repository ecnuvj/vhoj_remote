package com.vjudge.ecnuvj.remote.provider.spoj;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
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
public class SPOJQuerier extends AuthenticatedQuerier {

    @Override
    public RemoteOjInfo getOjInfo() {
        return SPOJInfo.INFO;
    }

    @Override
    protected SubmissionRemoteStatus query(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        String html = client.post( //
                "/status/ajax=1,ajaxdiff=1", //
                SimpleNameValueEntityFactory.create("ids", info.remoteRunId, getOjInfo().defaultChaset) //
        ).getBody();
        html = html.replaceAll("\\\\[nt]", "").replaceAll(">(run|edit|ideone it)<", "><").replaceAll("<.*?>", "").replace("&nbsp;", "").trim();

        Pattern pattern = Pattern.compile("\"status_description\":\"(.+?)\", \"id\":" + info.remoteRunId + ", \"status\":.+?,\"time\":\"(.+?)\",\"mem\":\"([\\d\\D]+?)\"");
        Matcher matcher = pattern.matcher(html);
        Validate.isTrue(matcher.find());

        SubmissionRemoteStatus status = new SubmissionRemoteStatus();
        status.rawStatus = StringUtils.capitalize(matcher.group(1).trim());
        status.statusType = SubstringNormalizer.DEFAULT.getStatusType(status.rawStatus);
        if (status.statusType == RemoteStatusType.AC) {
            int mul = matcher.group(3).contains("M") ? 1024 : 1;
            status.executionMemory = (int) (0.5 + mul * Double.parseDouble(matcher.group(3).replaceAll("[Mk]", "").trim()));
            status.executionTime = (int) (0.5 + 1000 * Double.parseDouble(matcher.group(2).trim()));
        } else if (status.statusType == RemoteStatusType.CE) {
            html = client.get("/error/" + info.remoteRunId).getBody();
            status.compilationErrorInfo = Tools.regFind(html, "<div align=\"left\">(<pre><small>[\\s\\S]*?</small></pre>)");
        }
        return status;
    }

}
