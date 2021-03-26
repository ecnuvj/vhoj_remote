package com.vjudge.ecnuvj.remote.provider.aizu;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpBodyValidator;
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
public class AizuSubmitter extends ComplexSubmitter {

    @Override
    public RemoteOjInfo getOjInfo() {
        return AizuInfo.INFO;
    }


    @Override
    protected boolean needLogin() {
        return false;
    }

    @Override
    protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
        String html = client.get("/onlinejudge/status.jsp").getBody();
        Matcher matcher = Pattern.compile("id=\"run_(\\d+)(?:[\\s\\S](?!\\/tr))*>" + info.remoteAccountId + "<(?:[\\s\\S](?!\\/tr))*description\\.jsp\\?id=" + info.remoteProblemId).matcher(html);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }

    @Override
    protected String submitCode(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        HttpEntity entity = SimpleNameValueEntityFactory.create(
                "language", info.remotelanguage, //
                "password", remoteAccount.getPassword(), //
                "problemNO", info.remoteProblemId, //
                "sourceCode", info.sourceCode, //
                "userID", remoteAccount.getAccountId() //
        );
        client.post("/onlinejudge/servlet/Submit", entity, new HttpBodyValidator("HTTP-EQUIV=\"refresh\""));
        return null;
    }

}
