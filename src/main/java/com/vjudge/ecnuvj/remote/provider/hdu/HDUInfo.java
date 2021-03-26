package com.vjudge.ecnuvj.remote.provider.hdu;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class HDUInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.HDU, //
            "HDU", //
            new HttpHost("acm.hdu.edu.cn") //
    );

    static {
        INFO.defaultChaset = "gb2312";
        INFO.maxInactiveInterval = 60000L;
        INFO.faviconUrl = "images/remote_oj/HDU_icon.png";
        INFO._64IntIoFormat = "%I64d & %I64u";
    }

}
