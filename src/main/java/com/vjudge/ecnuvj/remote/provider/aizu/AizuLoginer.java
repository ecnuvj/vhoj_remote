package com.vjudge.ecnuvj.remote.provider.aizu;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpBodyValidator;
import com.vjudge.ecnuvj.httpclient.HttpStatusValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.loginer.RetentiveLoginer;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class AizuLoginer extends RetentiveLoginer {

    @Override
    public RemoteOjInfo getOjInfo() {
        return AizuInfo.INFO;
    }


    @Override
    protected void loginEnforce(RemoteAccount account, DedicatedHttpClient client) {
        if (client.get("/onlinejudge/index.jsp").getBody().contains("href=\"logout.jsp\"")) {
            return;
        }

        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "loginUserID", account.getAccountId(), //
                "loginPassword", account.getPassword(), //
                "submit", "Sign in");
        client.post(
                "/onlinejudge/index.jsp",
                entity,
                HttpStatusValidator.SC_OK,
                new HttpBodyValidator("href=\"logout.jsp\""));
    }

}
