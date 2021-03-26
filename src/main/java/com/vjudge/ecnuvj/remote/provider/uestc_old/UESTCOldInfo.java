package com.vjudge.ecnuvj.remote.provider.uestc_old;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class UESTCOldInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.UESTCOld, //
            "UESTC-old", //
            new HttpHost("acm.uestc.edu.cn") //
    );

    static {
        INFO._64IntIoFormat = "%lld & %llu";
    }

}
