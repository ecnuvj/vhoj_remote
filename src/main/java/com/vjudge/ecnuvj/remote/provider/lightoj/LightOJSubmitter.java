package com.vjudge.ecnuvj.remote.provider.lightoj;

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
public class LightOJSubmitter extends ComplexSubmitter {

    @Override
    public RemoteOjInfo getOjInfo() {
        return LightOJInfo.INFO;
    }

    @Override
    protected boolean needLogin() {
        return true;
    }

    @Override
    protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
        String html = client.get("/volume_usersubmissions.php").getBody();
        Matcher matcher = Pattern.compile("sub_id=(\\d+)(?:[\\s\\S](?!/tr))*problem=" + info.remoteProblemId).matcher(html);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }

    @Override
    protected String submitCode(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        HttpEntity entity = SimpleNameValueEntityFactory.create(
                "language", info.remotelanguage, //
                "sub_problem", info.remoteProblemId, //
                "code", info.sourceCode //
        );
        client.post("/volume_submit.php", entity, new HttpBodyValidator("location.href='volume_usersubmissions.php'"));
        return null;
    }

}
