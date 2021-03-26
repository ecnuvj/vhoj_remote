package com.vjudge.ecnuvj.remote.provider.zoj;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class ZOJInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.ZOJ, //
            "ZOJ", //
            new HttpHost("acm.zju.edu.cn") //
    );

    static {
        INFO.faviconUrl = "images/remote_oj/ZOJ_favicon.ico";
        INFO._64IntIoFormat = "%lld & %llu";
        INFO.urlForIndexDisplay = "http://acm.zju.edu.cn/onlinejudge/";
    }

}
