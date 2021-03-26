package com.vjudge.ecnuvj.remote.provider.poj;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpStatusValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.submitter.ComplexSubmitter;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class POJSubmitter extends ComplexSubmitter {

    @Override
    public RemoteOjInfo getOjInfo() {
        return POJInfo.INFO;
    }

    @Override
    protected boolean needLogin() {
        return true;
    }

    @Override
    protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
        String html = client.get("/status?user_id=" + info.remoteAccountId + "&problem_id=" + info.remoteProblemId).getBody();
        Matcher matcher = Pattern.compile("<tr align=center><td>(\\d+)").matcher(html);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }

    @Override
    protected String submitCode(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        HttpEntity entity = SimpleNameValueEntityFactory.create(
                "language", info.remotelanguage, //
                "problem_id", info.remoteProblemId, //
                "source", new String(Base64.encodeBase64(info.sourceCode.getBytes())),
                "encoded", "1",
                "submit", "Submit"
        );
        client.post("/submit", entity, HttpStatusValidator.SC_MOVED_TEMPORARILY);
        return null;
    }

}
