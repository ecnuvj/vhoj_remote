package com.vjudge.ecnuvj.remote.provider.local;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.SimpleHttpResponse;
import com.vjudge.ecnuvj.httpclient.SimpleHttpResponseValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.loginer.RetentiveLoginer;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class LOCALLoginer extends RetentiveLoginer {

    @Override
    public RemoteOjInfo getOjInfo() {
        return LOCALInfo.INFO;
    }

    @Override
    protected void loginEnforce(RemoteAccount account, DedicatedHttpClient client) {
        if (client.get(LOCALInfo.getPath()).getBody().contains("<a href=logout.php>")) {
            return;
        }

        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "user_id", account.getAccountId(), //
                "password", account.getPassword());
        client.get(LOCALInfo.getPath() + "/setlang.php?lang=en");
        client.post(LOCALInfo.getPath() + "/login.php", entity, new SimpleHttpResponseValidator() {
            @Override
            public void validate(SimpleHttpResponse response) throws Exception {
                Validate.isTrue(response.getBody().contains("history.go(-2)"));
            }
        });
    }

}
