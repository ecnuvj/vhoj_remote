package com.vjudge.ecnuvj.remote.provider.hust;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class HUSTInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.HUST, //
            "HUST", //
            new HttpHost("acm.hust.edu.cn") //
    );

    static {
        INFO.faviconUrl = "images/remote_oj/HUST_icon.jpg";
        INFO._64IntIoFormat = "%lld & %llu";
    }
}
