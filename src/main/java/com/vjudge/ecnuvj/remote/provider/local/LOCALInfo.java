package com.vjudge.ecnuvj.remote.provider.local;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import org.apache.http.HttpHost;

import java.util.ResourceBundle;

public class LOCALInfo {

    private static String domain = ResourceBundle.getBundle("application").getString("hustoj.domain");
    private static int port = Integer.parseInt(ResourceBundle.getBundle("application").getString("hustoj.port"));
    private static String path = ResourceBundle.getBundle("application").getString("hustoj.path");


    public static String getDomain() {
        return domain;
    }


    public static void setDomain(String domain) {
        LOCALInfo.domain = domain;
    }


    public static String getPath() {
        return path;
    }


    public static void setPath(String path) {
        LOCALInfo.path = path;
    }


    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.LOCAL, //
            "LOCAL", //
            new HttpHost(domain, port) //
    );


    static {
        INFO.faviconUrl = "images/remote_oj/icon-icpc-small.gif";
        INFO._64IntIoFormat = "%lld & %llu";
        INFO.urlForIndexDisplay = "http://" + domain + path;
    }


    public static int getPort() {
        // TODO Auto-generated method stub
        return port;
    }
}
