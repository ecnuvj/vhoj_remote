package com.vjudge.ecnuvj.remote.provider.tkoj;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

/**
 * Created with IDEA
 * User: zzzz76
 * Date: 2020-12-10
 */
public class TKOJInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.TKOJ, //
            "TKOJ", //
            new HttpHost("tk.hustoj.com") //
    );

    static {
        INFO.faviconUrl = "images/remote_oj/TKOJ_favicon.ico";
        INFO._64IntIoFormat = "%I64d & %I64u";
    }
}
