package com.vjudge.ecnuvj.remote.provider.csu;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpBodyValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.submitter.ComplexSubmitter;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CSUSubmitter extends ComplexSubmitter {

    @Override
    public RemoteOjInfo getOjInfo() {
        return CSUInfo.INFO;
    }

    @Override
    protected boolean needLogin() {
        return true;
    }

    @Override
    protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
        return info.remoteRunId != null ? Integer.parseInt(info.remoteRunId) : -1;
    }

    @Override
    protected String submitCode(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        HttpEntity entity = SimpleNameValueEntityFactory.create(
                "language", info.remotelanguage, //
                "pid", info.remoteProblemId, //
                "source", info.sourceCode
        );
        HttpPost post = new HttpPost("/csuoj/Problemset/submit_ajax");
        post.setEntity(entity);
        post.setHeader("X-Requested-With", "XMLHttpRequest");
        String html = client.execute(post,
                new HttpBodyValidator("\"msg\":\"Submit successful")).getBody();
        Matcher matcher = Pattern.compile("\"solution_id\":\"(\\d+)\"").matcher(html);
        if (matcher.find()) {
            info.remoteRunId = matcher.group(1);
        }
        return null;
    }

}
