package com.vjudge.ecnuvj.remote.provider.codeforces;

import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.shared.codeforces.CFStyleLoginer;
import org.springframework.stereotype.Component;

@Component
public class CodeForcesLoginer extends CFStyleLoginer {

    @Override
    public RemoteOjInfo getOjInfo() {
        return CodeForcesInfo.INFO;
    }
}
