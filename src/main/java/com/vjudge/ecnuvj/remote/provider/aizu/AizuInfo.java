package com.vjudge.ecnuvj.remote.provider.aizu;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class AizuInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.Aizu, //
            "Aizu", //
            new HttpHost("judge.u-aizu.ac.jp") //
    );

    static {
        INFO.faviconUrl = "images/remote_oj/Aizu_favicon.ico";
        INFO._64IntIoFormat = "%lld & %llu";
    }

}
