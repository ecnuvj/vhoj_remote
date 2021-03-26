package com.vjudge.ecnuvj.remote.provider.tkoj;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpBodyValidator;
import com.vjudge.ecnuvj.httpclient.HttpStatusValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.loginer.RetentiveLoginer;
import com.vjudge.ecnuvj.tool.MD5;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Component;

/**
 * Created with IDEA
 * User: zzzz76
 * Date: 2020-12-10
 */
@Component
public class TKOJLoginer extends RetentiveLoginer {
    @Override
    public RemoteOjInfo getOjInfo() {
        return TKOJInfo.INFO;
    }

    @Override
    protected void loginEnforce(RemoteAccount account, DedicatedHttpClient client) throws Exception {
        if (client.get("/loginpage.php", HttpStatusValidator.SC_OK).getBody().contains("Please logout First!")) {
            return;
        }

        HttpEntity entity = SimpleNameValueEntityFactory.create(
                "user_id", account.getAccountId(),
                "password", MD5.getMD5(account.getPassword()),
                "csrf", TKOJVerifyUtil.getCsrf(client),
                "vcode", TKOJVerifyUtil.getCaptcha(client)
        );
        HttpPost post = new HttpPost("/login.php");
        post.setEntity(entity);
        client.execute(post, HttpStatusValidator.SC_OK, new HttpBodyValidator("history.go(-2);"));
    }

}
