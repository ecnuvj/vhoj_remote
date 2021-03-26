package com.vjudge.ecnuvj.remote.account;

import com.vjudge.ecnuvj.remote.account.config.RemoteAccountOJConfig;
import com.vjudge.ecnuvj.remote.common.RemoteOj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Short for remote account task executor
 *
 * @author Isun
 */
public class RemoteAccountTaskExecutor {
    private final static Logger log = LoggerFactory.getLogger(RemoteAccountTaskExecutor.class);

    /**
     * remoteOj -> repo dedicating to one OJ
     */
    private Map<RemoteOj, RemoteAccountRepository> repos =
            new HashMap<RemoteOj, RemoteAccountRepository>();

    /**
     * <pre>A RemoteAccountTask must enter this queue twice for:
     * 1st. Apply for an account and execute;
     * 2nd. Return the account, whatever that task finishes normally.
     * </pre>
     */
    private LinkedBlockingQueue<RemoteAccountTask<?>> running =
            new LinkedBlockingQueue<RemoteAccountTask<?>>();

    ////////////////////////////////////////////////////////////////

    public RemoteAccountTaskExecutor(Map<RemoteOj, RemoteAccountOJConfig> config) {
        for (RemoteOj remoteOj : config.keySet()) {
            RemoteAccountOJConfig ojConfig = config.get(remoteOj);
            repos.put(remoteOj, new RemoteAccountRepository(remoteOj, ojConfig, this));
        }
    }

    ////////////////////////////////////////////////////////////////

    // 由守护线程(守护提交线程) 负责 处理阻塞队列中的消息
    public void init() {
        Thread executingThead = new Thread() {
            public void run() {
                while (true) {
                    RemoteAccountTask<?> task = null;
                    try {
                        task = running.take();
                        RemoteOj remoteOj = task.getRemoteOj();

                        // 假设在处理一个提交任务时，存在多个账户可以选择
                        RemoteAccountRepository repo = repos.get(remoteOj);
                        if (repo != null) {
                            repo.handle(task);
                        } else {
                            throw new RuntimeException("There are no remote accounts configured for remote OJ: " + remoteOj);
                        }
                    } catch (Throwable t) {
                        log.error(t.getMessage(), t);
                        if (task != null) {
                            try {
                                task.getHandler().onError(t);
                            } catch (Throwable t1) {
                                log.error(t.getMessage(), t1);
                            }
                        }
                    }
                }
            }
        };
        executingThead.setDaemon(true);
        executingThead.start();
    }

    public void submit(RemoteAccountTask<?> task) {
        running.offer(task);
    }

}
