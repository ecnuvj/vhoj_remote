package com.vjudge.ecnuvj.remote.provider.nbut;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class NBUTInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.NBUT, //
            "NBUT", //
            new HttpHost("ac.2333.moe", 443, "https") //
    );

    static {
        INFO.faviconUrl = "images/remote_oj/NBUT_icon.jpg";
    }
}
