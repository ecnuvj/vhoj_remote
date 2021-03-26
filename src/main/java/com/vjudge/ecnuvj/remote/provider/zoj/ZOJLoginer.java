package com.vjudge.ecnuvj.remote.provider.zoj;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpStatusValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.loginer.RetentiveLoginer;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class ZOJLoginer extends RetentiveLoginer {

    @Override
    public RemoteOjInfo getOjInfo() {
        return ZOJInfo.INFO;
    }

    @Override
    protected void loginEnforce(RemoteAccount account, DedicatedHttpClient client) {
        if (client.get("/onlinejudge/").getBody().contains("/logout.do")) {
            return;
        }

        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "handle", account.getAccountId(), //
                "password", account.getPassword(), //
                "rememberMe", "on");
        client.post("/onlinejudge/login.do", entity, HttpStatusValidator.SC_MOVED_TEMPORARILY);
    }

}
