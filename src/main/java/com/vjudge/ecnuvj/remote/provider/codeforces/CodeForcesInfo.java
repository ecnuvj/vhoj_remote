package com.vjudge.ecnuvj.remote.provider.codeforces;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

public class CodeForcesInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.CodeForces, //
            "CodeForces", //
            new HttpHost("codeforces.com") //
    );

    static {
        INFO.faviconUrl = "images/remote_oj/CodeForces_favicon.png";
        INFO._64IntIoFormat = "%I64d & %I64u";
    }
}
