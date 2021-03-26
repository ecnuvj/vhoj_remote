package com.vjudge.ecnuvj.remote.provider.acdream;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpBodyValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.loginer.RetentiveLoginer;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class ACdreamLoginer extends RetentiveLoginer {

    @Override
    public RemoteOjInfo getOjInfo() {
        return ACdreamInfo.INFO;
    }

    @Override
    protected void loginEnforce(RemoteAccount account, DedicatedHttpClient client) {
        if (client.get("/").getBody().contains(">Logout<")) {
            return;
        }

        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "remember", "true", //
                "password", account.getPassword(), //
                "username", account.getAccountId());
        client.post("/login", entity, new HttpBodyValidator("1", true));
    }

}
