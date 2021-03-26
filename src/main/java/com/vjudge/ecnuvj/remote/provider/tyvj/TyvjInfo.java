package com.vjudge.ecnuvj.remote.provider.tyvj;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class TyvjInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.Tyvj, //
            "Tyvj", //
            new HttpHost("www.tyvj.cn") //
    );

    static {
        INFO.faviconUrl = "images/remote_oj/Tyvj_favicon.ico";
        INFO._64IntIoFormat = "%I64d & %I64u";
    }

}
