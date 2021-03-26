package com.vjudge.ecnuvj.remote.provider.lightoj;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpBodyValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.loginer.RetentiveLoginer;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class LightOJLoginer extends RetentiveLoginer {

    @Override
    public RemoteOjInfo getOjInfo() {
        return LightOJInfo.INFO;
    }

    @Override
    public void loginEnforce(RemoteAccount account, DedicatedHttpClient client) {
        if (!client.get("/index.php").getBody().contains("<script>location.href=")) {
            return;
        }

        HttpEntity entity = SimpleNameValueEntityFactory.create(
                "mypassword", account.getPassword(),
                "myrem", "1",
                "myuserid", account.getAccountId()
        );
        client.post("/login_check.php", entity, new HttpBodyValidator("login_success.php"));
    }

}
