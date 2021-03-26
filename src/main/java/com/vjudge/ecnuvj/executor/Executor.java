package com.vjudge.ecnuvj.executor;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author 14861
 */
@Component
public class Executor {

    private Map<ExecutorTaskType, ThreadPoolExecutor> executors = new HashMap<ExecutorTaskType, ThreadPoolExecutor>();
    private ScheduledExecutorService delayedTaskDispatcher = Executors.newScheduledThreadPool(1);

    protected <V> Future<V> submitNoDelay(final Task<V> task) {
        Validate.isTrue(task.delaySeconds <= 0);
        return getExecutor(task.taskType).submit(task);
    }

    protected <V> ScheduledFuture<Future<V>> submitDelay(final Task<V> task) {
        Validate.isTrue(task.delaySeconds > 0);
        return delayedTaskDispatcher.schedule(new Callable<Future<V>>() {
            @Override
            public Future<V> call() throws Exception {
                return getExecutor(task.taskType).submit(task);
            }
        }, task.delaySeconds, TimeUnit.SECONDS);
    }

    private ThreadPoolExecutor getExecutor(ExecutorTaskType taskType) {
        if (!executors.containsKey(taskType)) {
            synchronized (executors) {
                if (!executors.containsKey(taskType)) {
                    ThreadPoolExecutor executor = new ThreadPoolExecutor(
                            taskType.maximumConcurrency,
                            Integer.MAX_VALUE,
                            taskType.keepAliveSeconds,
                            TimeUnit.SECONDS,
                            new LinkedBlockingDeque<Runnable>());
                    executor.allowCoreThreadTimeOut(true);
                    executors.put(taskType, executor);
                }
            }
        }
        return executors.get(taskType);
    }

    @PreDestroy
    public void shutdown() {
        delayedTaskDispatcher.shutdownNow();
        for (ThreadPoolExecutor executor : executors.values()) {
            executor.shutdownNow();
        }
    }

}
