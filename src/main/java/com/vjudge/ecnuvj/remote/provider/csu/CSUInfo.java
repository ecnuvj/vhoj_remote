package com.vjudge.ecnuvj.remote.provider.csu;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class CSUInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.CSU, //
            "CSU", //
            new HttpHost("acm.csu.edu.cn") //
    );

    static {
        INFO.faviconUrl = "images/remote_oj/CSU_favicon.ico";
        INFO._64IntIoFormat = "%lld & %llu";
    }
}
