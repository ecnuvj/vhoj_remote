package com.vjudge.ecnuvj.remote.querier;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.tool.SpringBean;
import com.vjudge.ecnuvj.tool.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public class QueriersHolder {
    private final static Logger log = LoggerFactory.getLogger(QueriersHolder.class);

    private static HashMap<RemoteOj, Querier> queriers = new HashMap<RemoteOj, Querier>();

    public static Querier getQuerier(RemoteOj remoteOj) {
        if (!queriers.containsKey(remoteOj)) {
            synchronized (queriers) {
                if (!queriers.containsKey(remoteOj)) {
                    try {
                        List<Class<? extends Querier>> querierClasses = Tools.findSubClasses("com.vjudge.ecnuvj", Querier.class);
                        for (Class<? extends Querier> querierClass : querierClasses) {
                            Querier querier = SpringBean.getBean(querierClass);
                            queriers.put(querier.getOjInfo().remoteOj, querier);
                        }
                    } catch (Throwable t) {
                        log.error(t.getMessage(), t);
                        System.exit(-1);
                    }
                }
            }
        }
        return queriers.get(remoteOj);
    }

}
