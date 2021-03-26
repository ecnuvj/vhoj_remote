package com.vjudge.ecnuvj.remote.provider.codeforcesgym;

import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.shared.codeforces.CFStyleLoginer;
import org.springframework.stereotype.Component;

@Component
public class CodeForcesGymLoginer extends CFStyleLoginer {

    @Override
    public RemoteOjInfo getOjInfo() {
        return CodeForcesGymInfo.INFO;
    }

}
