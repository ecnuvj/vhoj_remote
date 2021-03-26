package com.vjudge.ecnuvj.remote.provider.tyvj;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpStatusValidator;
import com.vjudge.ecnuvj.httpclient.SimpleHttpResponse;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.loginer.RetentiveLoginer;
import com.vjudge.ecnuvj.tool.Tools;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Component;

@Component
public class TyvjLoginer extends RetentiveLoginer {

    @Override
    public RemoteOjInfo getOjInfo() {
        return TyvjInfo.INFO;
    }

    @Override
    protected void loginEnforce(RemoteAccount account, DedicatedHttpClient client) {
        SimpleHttpResponse response = client.get("/");

        if (response.getBody().contains("javascript:$('#frmLogout').submit();")) {
            return;
        }

        String token = Tools.regFind(response.getBody(),
                "<input name=\"__RequestVerificationToken\" type=\"hidden\" value=\"(.+?)\" /> ");
        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "__RequestVerificationToken", token,
                "Username", account.getAccountId(),
                "Password", account.getPassword(),
                "Remember", "false");

        HttpPost post = new HttpPost("/Login");
        post.setEntity(entity);
        post.setHeader("Referer", getOjInfo().mainHost.toURI());

        client.execute(post, HttpStatusValidator.SC_MOVED_TEMPORARILY);
    }

}
