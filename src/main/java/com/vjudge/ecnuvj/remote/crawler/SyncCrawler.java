package com.vjudge.ecnuvj.remote.crawler;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClientFactory;
import com.vjudge.ecnuvj.tool.Handler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * No dependence on any other resource, just do it in invoking thread.
 *
 * @author Isun
 */
public abstract class SyncCrawler implements Crawler {

    @Autowired
    protected DedicatedHttpClientFactory dedicatedHttpClientFactory;

    @Override
    public void crawl(String problemId, Handler<RawProblemInfo> handler) throws Exception {
        RawProblemInfo info = null;
        try {
            info = crawl(problemId);
        } catch (Throwable t) {
            handler.onError(t);
            return;
        }
        handler.handle(info);
    }

    abstract protected RawProblemInfo crawl(String problemId) throws Exception;

}
