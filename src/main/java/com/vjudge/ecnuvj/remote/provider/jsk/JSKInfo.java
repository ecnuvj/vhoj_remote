package com.vjudge.ecnuvj.remote.provider.jsk;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

/**
 * Created with IDEA
 * User: zzzz76
 * Date: 2020-10-03
 */
public class JSKInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo(
            RemoteOj.JSK,
            "JSK",
            new HttpHost("nanti.jisuanke.com", 443, "https")
    );

    public static final RemoteOjInfo LOGIN = new RemoteOjInfo(
            RemoteOj.JSK,
            "JSK",
            new HttpHost("passport.jisuanke.com", 443, "https")
    );

    static {
        INFO.faviconUrl = "images/remote_oj/JSK_icon.ico";
        LOGIN.faviconUrl = "images/remote_oj/JSK_icon.ico";
    }
}
