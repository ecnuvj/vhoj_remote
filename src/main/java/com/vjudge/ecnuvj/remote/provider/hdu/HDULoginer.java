package com.vjudge.ecnuvj.remote.provider.hdu;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpStatusValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.loginer.RetentiveLoginer;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class HDULoginer extends RetentiveLoginer {

    @Override
    public RemoteOjInfo getOjInfo() {
        return HDUInfo.INFO;
    }

    @Override
    protected void loginEnforce(RemoteAccount account, DedicatedHttpClient client) {
        if (client.get("/").getBody().contains("href=\"/userloginex.php?action=logout\"")) {
            return;
        }

        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "username", account.getAccountId(), //
                "userpass", account.getPassword(), //
                "gb2312");
        client.post(
                "/userloginex.php?action=login&cid=0&notice=0",
                entity,
                HttpStatusValidator.SC_MOVED_TEMPORARILY);
    }

}
