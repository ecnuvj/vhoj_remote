package com.vjudge.ecnuvj.remote.status;

public interface RemoteStatusNormalizer {

    RemoteStatusType getStatusType(String rawStatus);

}
