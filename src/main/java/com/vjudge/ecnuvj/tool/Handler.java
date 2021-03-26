package com.vjudge.ecnuvj.tool;

public interface Handler<V> {

    void handle(V v);

    void onError(Throwable t);

}
