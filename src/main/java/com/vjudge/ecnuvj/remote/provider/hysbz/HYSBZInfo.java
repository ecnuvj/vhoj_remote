package com.vjudge.ecnuvj.remote.provider.hysbz;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class HYSBZInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.HYSBZ, //
            "HYSBZ", //
            new HttpHost("www.lydsy.com") //
    );

    static {
        INFO.faviconUrl = "images/remote_oj/HYSBZ_icon.png";
        INFO._64IntIoFormat = "%lld & %llu";
        INFO.urlForIndexDisplay = "http://www.lydsy.com/JudgeOnline/";
    }
}
