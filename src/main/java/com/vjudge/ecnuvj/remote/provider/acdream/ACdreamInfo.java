package com.vjudge.ecnuvj.remote.provider.acdream;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class ACdreamInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.ACdream, //
            "ACdream", //
            new HttpHost("acdream.info") //
    );

    static {
        INFO.faviconUrl = "images/remote_oj/ACdream_favicon.ico";
        INFO._64IntIoFormat = "%lld & %llu";
    }

}
