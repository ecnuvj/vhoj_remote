package com.vjudge.ecnuvj.remote.crawler;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.tool.SpringBean;
import com.vjudge.ecnuvj.tool.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public class CrawlersHolder {
    private final static Logger log = LoggerFactory.getLogger(CrawlersHolder.class);

    private static HashMap<RemoteOj, Crawler> crawlers = new HashMap<RemoteOj, Crawler>();

    public static Crawler getCrawler(RemoteOj remoteOj) {
        if (!crawlers.containsKey(remoteOj)) {
            synchronized (crawlers) {
                if (!crawlers.containsKey(remoteOj)) {
                    try {
                        List<Class<? extends Crawler>> crawlerClasses = Tools.findSubClasses("com.vjudge.ecnuvj", Crawler.class);
                        for (Class<? extends Crawler> crawlerClass : crawlerClasses) {
                            Crawler crawler = SpringBean.getBean(crawlerClass);
                            crawlers.put(crawler.getOjInfo().remoteOj, crawler);
                        }
                    } catch (Throwable t) {
                        log.error(t.getMessage(), t);
                        System.exit(-1);
                    }
                }
            }
        }
        return crawlers.get(remoteOj);
    }

}
