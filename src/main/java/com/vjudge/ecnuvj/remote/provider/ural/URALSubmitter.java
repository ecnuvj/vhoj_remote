package com.vjudge.ecnuvj.remote.provider.ural;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpStatusValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.submitter.ComplexSubmitter;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class URALSubmitter extends ComplexSubmitter {

    @Override
    public RemoteOjInfo getOjInfo() {
        return URALInfo.INFO;
    }

    @Override
    protected boolean needLogin() {
        return false;
    }

    @Override
    protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
        String html = client.get("/status.aspx?space=1&num=" + info.remoteProblemId + "&author=" + info.remoteAccountId.replaceAll("\\D", "")).getBody();
        Matcher matcher = Pattern.compile("getsubmit\\.aspx/(\\d+)").matcher(html);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }

    @Override
    protected String submitCode(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "Action", "submit", //
                "Language", info.remotelanguage, //
                "ProblemNum", info.remoteProblemId, //
                "Source", info.sourceCode, //
                "JudgeID", remoteAccount.getAccountId(), //
                "SpaceID", "1" //
        );
        client.post("/submit.aspx", entity, HttpStatusValidator.SC_OK);
        return null;
    }

}
