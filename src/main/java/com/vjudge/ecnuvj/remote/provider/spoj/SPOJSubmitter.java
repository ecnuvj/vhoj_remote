package com.vjudge.ecnuvj.remote.provider.spoj;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.submitter.ComplexSubmitter;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import com.vjudge.ecnuvj.tool.Tools;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class SPOJSubmitter extends ComplexSubmitter {

    @Override
    public RemoteOjInfo getOjInfo() {
        return SPOJInfo.INFO;
    }

    @Override
    protected boolean needLogin() {
        return true;
    }

    @Override
    protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
        return submitted ? Integer.parseInt(info.remoteRunId) : -1;
    }

    @Override
    protected String submitCode(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        HttpEntity entity = SimpleNameValueEntityFactory.create(
                "lang", info.remotelanguage, //
                "problemcode", info.remoteProblemId, //
                "file", info.sourceCode, //
                getCharset() //
        );
        String html = client.post("/submit/complete/", entity).getBody();

        if (html.contains("submit in this language for this problem")) {
            return "Language Error";
        }
        if (html.contains("solution is too long")) {
            return "Code Length Exceeded";
        }

        info.remoteRunId = Tools.regFind(html, "name=\"newSubmissionId\" value=\"(\\d+)\"");
        Validate.isTrue(!StringUtils.isBlank(info.remoteRunId));
        return null;
    }

}
