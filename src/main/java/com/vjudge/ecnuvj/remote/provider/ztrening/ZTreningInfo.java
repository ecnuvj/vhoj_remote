package com.vjudge.ecnuvj.remote.provider.ztrening;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class ZTreningInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.ZTrening, //
            "Z-Trening", //
            new HttpHost("www.z-trening.com") //
    );

    static {
        INFO._64IntIoFormat = "%lld & %llu";
    }

}
