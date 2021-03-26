package com.vjudge.ecnuvj.remote.provider.sgu;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class SGUInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.SGU, //
            "SGU", //
            new HttpHost("acm.sgu.ru") //
    );

    static {
        INFO.faviconUrl = "images/remote_oj/SGU_favicon.ico";
        INFO._64IntIoFormat = "%I64d & %I64u";
    }
}
