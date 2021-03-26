package com.vjudge.ecnuvj.remote.loginer;

import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjAware;

/**
 * Implementation should be stateless.
 *
 * @author Isun
 */
public interface Loginer extends RemoteOjAware {

    void login(RemoteAccount account) throws Exception;

}
