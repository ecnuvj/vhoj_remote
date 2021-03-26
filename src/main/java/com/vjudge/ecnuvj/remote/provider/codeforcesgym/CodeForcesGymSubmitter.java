package com.vjudge.ecnuvj.remote.provider.codeforcesgym;

import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.shared.codeforces.CFStyleQuerier;
import org.springframework.stereotype.Component;

@Component
public class CodeForcesGymSubmitter extends CFStyleQuerier {

    @Override
    public RemoteOjInfo getOjInfo() {
        return CodeForcesGymInfo.INFO;
    }
}
