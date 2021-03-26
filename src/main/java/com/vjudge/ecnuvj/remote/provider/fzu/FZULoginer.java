package com.vjudge.ecnuvj.remote.provider.fzu;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpBodyValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.loginer.RetentiveLoginer;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class FZULoginer extends RetentiveLoginer {

    @Override
    public RemoteOjInfo getOjInfo() {
        return FZUInfo.INFO;
    }

    @Override
    protected void loginEnforce(RemoteAccount account, DedicatedHttpClient client) {
        if (client.get("/index.php").getBody().contains(">Logout</a>")) {
            return;
        }

        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "uname", account.getAccountId(), //
                "upassword", account.getPassword());
        client.post("/login.php?act=1", entity, new HttpBodyValidator("location.replace(\"index.php\")"));
    }

}
