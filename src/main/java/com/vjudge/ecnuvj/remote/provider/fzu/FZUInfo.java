package com.vjudge.ecnuvj.remote.provider.fzu;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class FZUInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.FZU, //
            "FZU", //
            new HttpHost("acm.fzu.edu.cn") //
    );

    static {
        INFO.faviconUrl = "images/remote_oj/FZU_favicon.gif";
        INFO._64IntIoFormat = "%I64d & %I64u";
    }
}
