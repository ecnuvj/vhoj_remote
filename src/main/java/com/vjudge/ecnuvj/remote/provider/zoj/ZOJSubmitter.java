package com.vjudge.ecnuvj.remote.provider.zoj;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.submitter.ComplexSubmitter;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import com.vjudge.ecnuvj.tool.Tools;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ZOJSubmitter extends ComplexSubmitter {

    @Override
    public RemoteOjInfo getOjInfo() {
        return ZOJInfo.INFO;
    }

    @Override
    protected boolean needLogin() {
        return true;
    }

    @Override
    protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
        String html = client.get(
                "/onlinejudge/showRuns.do?contestId=1&problemCode=" + info.remoteProblemId + "&handle=" + info.remoteAccountId)
                .getBody();
        Matcher matcher = Pattern.compile("<td class=\"runId\">(\\d+)</td>").matcher(html);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }

    @Override
    protected String submitCode(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) throws InterruptedException {
        String html = client.get("/onlinejudge/showProblem.do?problemCode=" + info.remoteProblemId).getBody();
        String realProblemId = Tools.regFind(html, "problemId=([\\s\\S]*?)\"><font");

        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "languageId", info.remotelanguage, //
                "problemId", realProblemId, //
                "source", info.sourceCode);
        client.post("/onlinejudge/submit.do", entity);
        return null;
    }

    @Override
    protected long getSubmitReceiptDelay() {
        return 30000;
    }

}
