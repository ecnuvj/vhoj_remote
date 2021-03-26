package com.vjudge.ecnuvj.remote.provider.sgu;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.SimpleHttpResponse;
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
public class SGUSubmitter extends ComplexSubmitter {

    @Override
    public RemoteOjInfo getOjInfo() {
        return SGUInfo.INFO;
    }

    @Override
    protected boolean needLogin() {
        return false;
    }

    @Override
    protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
        String html = client.get("/status.php?id=" + info.remoteAccountId).getBody();
        Matcher matcher = Pattern.compile("<TD>(\\d{7,})</TD>(?:[\\s\\S](?!TR))*" + info.remoteProblemId).matcher(html);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }

    @Override
    protected String submitCode(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "elang", info.remotelanguage, //
                "id", remoteAccount.getAccountId(), //
                "pass", remoteAccount.getPassword(), //
                "problem", info.remoteProblemId, //
                "source", info.sourceCode
        );

        SimpleHttpResponse response = client.post("/sendfile.php?contest=0", entity);
        if (!response.getBody().contains("successfully submitted")) {
            if (response.getBody().contains("Your source seems to be dangerous")) {
                return "Dangerous Code Error";
            }
            throw new RuntimeException();
        }

        return null;
    }

}
