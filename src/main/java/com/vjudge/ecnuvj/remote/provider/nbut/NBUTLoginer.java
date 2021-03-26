package com.vjudge.ecnuvj.remote.provider.nbut;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpBodyValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.loginer.RetentiveLoginer;
import com.vjudge.ecnuvj.tool.Tools;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class NBUTLoginer extends RetentiveLoginer {

    @Override
    public RemoteOjInfo getOjInfo() {
        return NBUTInfo.INFO;
    }

    @Override
    protected void loginEnforce(RemoteAccount account, DedicatedHttpClient client) {
        if (client.get("/").getBody().contains("title=\"登出\"")) {
            return;
        }

        String html = client.get("/User/login.xhtml?url=%2F").getBody();
        String ojVerify = Tools.regFind(html, "name=\"__OJVERIFY__\" value=\"(\\w+)\"");

        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "__OJVERIFY__", ojVerify, //
                "password", account.getPassword(), //
                "username", account.getAccountId());
        client.post("/User/chklogin.xhtml", entity, new HttpBodyValidator("1"));
    }

}
