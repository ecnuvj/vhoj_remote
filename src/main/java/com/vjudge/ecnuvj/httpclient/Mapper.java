package com.vjudge.ecnuvj.httpclient;

public interface Mapper<S, T> {

    T map(S value) throws Exception;

}
