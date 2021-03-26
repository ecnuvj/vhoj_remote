package com.vjudge.ecnuvj.remote.provider.poj;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpStatusValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.loginer.RetentiveLoginer;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class POJLoginer extends RetentiveLoginer {

    @Override
    public RemoteOjInfo getOjInfo() {
        return POJInfo.INFO;
    }

    @Override
    protected void loginEnforce(RemoteAccount account, DedicatedHttpClient client) {
        if (client.get("/").getBody().contains(">Log Out</a>")) {
            return;
        }

        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "B1", "login", //
                "password1", account.getPassword(), //
                "url", "%2F", //
                "user_id1", account.getAccountId());
        client.post("/login", entity, HttpStatusValidator.SC_MOVED_TEMPORARILY);
    }

}
