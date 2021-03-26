package com.vjudge.ecnuvj.remote.provider.lightoj;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class LightOJInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.LightOJ, //
            "LightOJ", //
            new HttpHost("lightoj.com") //
    );

    static {
        INFO.maxInactiveInterval = 0;
        INFO._64IntIoFormat = "%lld & %llu";
    }

}
