package com.vjudge.ecnuvj.remote.crawler;

import com.vjudge.ecnuvj.remote.common.RemoteOjAware;
import com.vjudge.ecnuvj.tool.Handler;

/**
 * Implementation should be stateless.
 *
 * @author Isun
 */
public interface Crawler extends RemoteOjAware {

    /**
     * @param problemId
     * @param handler
     * @throws Exception
     */
    void crawl(String problemId, Handler<RawProblemInfo> handler) throws Exception;

}
