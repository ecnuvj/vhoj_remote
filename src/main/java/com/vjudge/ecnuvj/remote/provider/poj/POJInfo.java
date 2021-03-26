package com.vjudge.ecnuvj.remote.provider.poj;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class POJInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.POJ, //
            "POJ", //
            new HttpHost("poj.org") //
    );

    static {
        INFO.faviconUrl = "images/remote_oj/poj.ico";
        INFO._64IntIoFormat = "%I64d & %I64u";
    }

}
