package com.vjudge.ecnuvj.remote.account;

import com.vjudge.ecnuvj.executor.Task;
import com.vjudge.ecnuvj.remote.account.config.RemoteAccountConfig;
import com.vjudge.ecnuvj.remote.account.config.RemoteAccountOJConfig;
import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.tool.Handler;
import org.apache.commons.lang3.Validate;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Containing remote accounts dedicating to one remote OJ
 *
 * @author Isun
 */
public class RemoteAccountRepository {
    private final static Logger log = LoggerFactory.getLogger(RemoteAccountRepository.class);

    private final RemoteOj remoteOj;

    /**
     * accountId -> acountStatus
     */
    private final Map<String, RemoteAccountStatus> publicRepo = new HashMap<>();

    /**
     * accountId -> acountStatus
     */
    private final Map<String, RemoteAccountStatus> privateRepo = new HashMap<>();

    /**
     * accountId == null, meaning any account is OK.
     */
    private final LinkedList<RemoteAccountTask<?>> normalBacklog = new LinkedList<>();

    /**
     * accountId != null, meaning specifying an account.
     * accountId -> tasks
     */
    private final Map<String, LinkedList<RemoteAccountTask<?>>> pickyBacklog = new HashMap<>();

    private final RemoteAccountTaskExecutor remoteAuthTaskExecutor;

    /////////////////////////////////////////////////////////////

    public RemoteAccountRepository(RemoteOj remoteOj, RemoteAccountOJConfig ojConfig, RemoteAccountTaskExecutor remoteAuthTaskExecutor) {
        this.remoteOj = remoteOj;
        this.remoteAuthTaskExecutor = remoteAuthTaskExecutor;

        for (RemoteAccountConfig accountConfig : ojConfig.accounts) {
            RemoteAccountStatus status = new RemoteAccountStatus(
                    remoteOj,
                    accountConfig.id,
                    accountConfig.password,
                    accountConfig.isPublic,
                    ojConfig.contextNumber);
            (accountConfig.isPublic ? publicRepo : privateRepo).put(accountConfig.id, status);
        }
    }

    /////////////////////////////////////////////////////////////

    public void handle(RemoteAccountTask<?> task) {
        if (task.isDone()) {
            releaseAccount(task.getAccount());
        } else {
            tryExecute(task);
        }
    }

    private void releaseAccount(RemoteAccount account) {
        Validate.isTrue(account.getRemoteOj().equals(remoteOj));
        String accountId = account.getAccountId();

        RemoteAccountStatus accountStatus;
        if (publicRepo.containsKey(accountId)) {
            accountStatus = publicRepo.get(accountId);
        } else if (privateRepo.containsKey(accountId)) {
            accountStatus = privateRepo.get(accountId);
        } else {
            return;
        }

        accountStatus.removeExclusiveCode(account.getExclusiveCode());
        accountStatus.returnContext(account.getContext());

        LinkedList<RemoteAccountTask<?>> backlogs = pickyBacklog.get(accountId);
        if (backlogs != null) {
            if (tryBacklog(backlogs)) {
                if (backlogs.isEmpty()) {
                    pickyBacklog.remove(accountId);
                }
                return;
            }
        }
        if (accountStatus.isPublic()) {
            tryBacklog(normalBacklog);
        }
    }

    private boolean tryBacklog(LinkedList<RemoteAccountTask<?>> backlog) {
        for (Iterator<RemoteAccountTask<?>> iterator = backlog.iterator(); iterator.hasNext(); ) {
            RemoteAccountTask<?> task = iterator.next();
            RemoteAccount account = findAccount(task.getAccountId(), task.getExclusiveCode());
            if (account != null) {
                iterator.remove();
                execute(task, account);
                return true;
            }
        }
        return false;
    }

    private void tryExecute(RemoteAccountTask<?> task) {
        String accountId = task.getAccountId();
        if (accountId != null && !publicRepo.containsKey(accountId) && !privateRepo.containsKey(accountId)) {
            task.offerResult(new RuntimeException("Specified accountId(" + accountId + ") not found."));
            return;
        }

        RemoteAccount account = findAccount(accountId, task.getExclusiveCode());
        if (account != null) {
            execute(task, account);
            return;
        }
        if (accountId == null) {
            normalBacklog.add(task);
            return;
        }
        LinkedList<RemoteAccountTask<?>> picky = pickyBacklog.get(accountId);
        if (picky == null) {
            picky = new LinkedList<>();
            pickyBacklog.put(accountId, picky);
        }
        picky.add(task);
    }

    private RemoteAccount findAccount(String accountId, String exclusiveCode) {
        List<RemoteAccountStatus> candidates = new ArrayList<>();
        if (accountId == null) {
            candidates.addAll(publicRepo.values());
        } else if (publicRepo.containsKey(accountId)) {
            candidates.add(publicRepo.get(accountId));
        } else if (privateRepo.containsKey(accountId)) {
            candidates.add(privateRepo.get(accountId));
        }
        Collections.shuffle(candidates);
        for (RemoteAccountStatus remoteAccountStatus : candidates) {
            if (remoteAccountStatus.eligible(accountId, exclusiveCode)) {
                HttpContext context = remoteAccountStatus.borrowContext();
                remoteAccountStatus.addExclusiveCode(exclusiveCode);
                return new RemoteAccount(
                        remoteOj,
                        remoteAccountStatus.getAccountId(),
                        remoteAccountStatus.getPassword(),
                        exclusiveCode,
                        context);
            }
        }
        return null;
    }

    private <V> void execute(final RemoteAccountTask<V> task, final RemoteAccount account) {
        new Task<V>(task.getExecutorTaskType()) {
            @Override
            public V call() throws Exception {
                V result = null;
                Throwable throwable = null;
                try {
                    task.setAccount(account);
                    try {
                        result = task.call(account);
                    } catch (Throwable t) {
                        throwable = t;
                        task.offerResult(throwable);
                    }
                    if (result != null) {
                        task.offerResult(result);
                    }
                } catch (Throwable t) {
                    log.error(t.getMessage(), t);
                } finally {
                    task.done();
                    remoteAuthTaskExecutor.submit(task);
                }

                Handler<V> handler = task.getHandler();
                if (handler != null) {
                    if (result != null) {
                        handler.handle(result);
                    } else if (throwable != null) {
                        handler.onError(throwable);
                    } else {
                        handler.onError(new RuntimeException("What the fuck ??"));
                    }
                }
                return null;
            }
        }.submit();
    }

}
