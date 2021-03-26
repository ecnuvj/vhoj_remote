package com.vjudge.ecnuvj.remote.provider.scu;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class SCUInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.SCU, //
            "SCU", //
            new HttpHost("acm.scu.edu.cn") //
    );

    static {
        INFO.defaultChaset = "GBK";
        INFO.faviconUrl = "images/remote_oj/SCU_favicon.ico";
        INFO._64IntIoFormat = "%lld & %llu";
    }

}
