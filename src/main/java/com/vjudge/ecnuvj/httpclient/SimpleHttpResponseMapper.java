package com.vjudge.ecnuvj.httpclient;

public interface SimpleHttpResponseMapper<T> extends Mapper<SimpleHttpResponse, T> {

    T map(SimpleHttpResponse response) throws Exception;

}
