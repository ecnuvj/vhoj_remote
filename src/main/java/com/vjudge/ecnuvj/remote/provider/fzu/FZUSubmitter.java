package com.vjudge.ecnuvj.remote.provider.fzu;

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
public class FZUSubmitter extends ComplexSubmitter {

    @Override
    public RemoteOjInfo getOjInfo() {
        return FZUInfo.INFO;
    }

    @Override
    protected boolean needLogin() {
        return true;
    }

    @Override
    protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
        String html = client.get("/log.php?user=" + info.remoteAccountId + "&pid=" + info.remoteProblemId).getBody();
        Matcher matcher = Pattern.compile("<tr onmouseover=\"hl\\(this\\);\" onmouseout=\"unhl\\(this\\);\" >\\s*<td>(\\d+)</td>").matcher(html);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }

    @Override
    protected String submitCode(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) throws InterruptedException {
        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "lang", info.remotelanguage, //
                "pid", info.remoteProblemId, //
                "code", info.sourceCode //
        );
        client.post("/submit.php?act=5", entity, new HttpBodyValidator("Your Program have been saved"));
        return null;
    }

    @Override
    protected long getSubmitReceiptDelay() {
        return 30000;
    }

}
