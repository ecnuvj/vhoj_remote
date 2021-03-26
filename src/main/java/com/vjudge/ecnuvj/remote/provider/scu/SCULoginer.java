package com.vjudge.ecnuvj.remote.provider.scu;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpBodyValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.loginer.RetentiveLoginer;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class SCULoginer extends RetentiveLoginer {

    @Override
    public RemoteOjInfo getOjInfo() {
        return SCUInfo.INFO;
    }

    @Override
    protected void loginEnforce(RemoteAccount account, DedicatedHttpClient client) {
        if (client.get("/soj/index.action").getBody().contains("href=\"logout.action\"")) {
            return;
        }

        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "back", "2", //
                "id", account.getAccountId(), //
                "password", account.getPassword(), //
                "submit", "login");
        client.post("/soj/login.action", entity, new HttpBodyValidator("window.history.go(-"));
    }

}
