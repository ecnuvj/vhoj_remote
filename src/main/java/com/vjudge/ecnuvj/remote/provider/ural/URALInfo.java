package com.vjudge.ecnuvj.remote.provider.ural;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class URALInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.URAL, //
            "URAL", //
            new HttpHost("acm.timus.ru") //
    );

    static {
        INFO.faviconUrl = "images/remote_oj/URAL_favicon.ico";
        INFO._64IntIoFormat = "%I64d & %I64u";
    }

}
