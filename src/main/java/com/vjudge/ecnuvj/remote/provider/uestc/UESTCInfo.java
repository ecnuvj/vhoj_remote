package com.vjudge.ecnuvj.remote.provider.uestc;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class UESTCInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.UESTC, //
            "UESTC", //
            new HttpHost("acm.uestc.edu.cn") //
    );

    static {
        INFO.faviconUrl = "images/remote_oj/UESTC_favicon.png";
        INFO._64IntIoFormat = "%lld & %llu";
    }
}
