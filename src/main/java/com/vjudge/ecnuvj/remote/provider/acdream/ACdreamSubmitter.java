package com.vjudge.ecnuvj.remote.provider.acdream;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.SimpleHttpResponse;
import com.vjudge.ecnuvj.httpclient.SimpleHttpResponseValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.submitter.ComplexSubmitter;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class ACdreamSubmitter extends ComplexSubmitter {

    @Override
    public RemoteOjInfo getOjInfo() {
        return ACdreamInfo.INFO;
    }

    @Override
    protected boolean needLogin() {
        return true;
    }

    @Override
    protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
        String html = client.get("/status?name=" + info.remoteAccountId + "&pid=" + info.remoteProblemId).getBody();
        Matcher matcher = Pattern.compile("class=\"\\w+\"><td>(\\d{5,})</td>").matcher(html);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }

    @Override
    protected String submitCode(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        HttpEntity entity = SimpleNameValueEntityFactory.create(
                "lang", info.remotelanguage, //
                "pid", info.remoteProblemId, //
                "code", info.sourceCode
        );
        client.post("/submit", entity, new SimpleHttpResponseValidator() {
            @Override
            public void validate(SimpleHttpResponse response) throws Exception {
                Map<String, String> json = new Gson().fromJson(response.getBody(), new TypeToken<HashMap<String, String>>() {
                }.getType());
                int ret = Integer.parseInt(json.get("ret"));
                Validate.isTrue(ret == 0);
            }
        });
        return null;
    }

}
