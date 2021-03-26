package com.vjudge.ecnuvj.remote.provider.spoj;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpStatusValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.loginer.RetentiveLoginer;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class SPOJLoginer extends RetentiveLoginer {

    @Override
    public RemoteOjInfo getOjInfo() {
        return SPOJInfo.INFO;
    }

    @Override
    protected void loginEnforce(RemoteAccount account, DedicatedHttpClient client) {
        if (client.get("/").getBody().contains("<a href=\"/logout\">")) {
            return;
        }

        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "login_user", account.getAccountId(), //
                "password", account.getPassword(), //
                "autologin", "1", //
                "submit", "Log In", //
                "ISO-8859-1");
        client.post("/?a=login", entity, HttpStatusValidator.SC_MOVED_TEMPORARILY);
    }

}
