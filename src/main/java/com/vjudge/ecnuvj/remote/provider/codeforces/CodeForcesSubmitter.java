package com.vjudge.ecnuvj.remote.provider.codeforces;

import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.shared.codeforces.CFStyleSubmitter;
import org.springframework.stereotype.Component;


@Component
public class CodeForcesSubmitter extends CFStyleSubmitter {

    @Override
    public RemoteOjInfo getOjInfo() {
        return CodeForcesInfo.INFO;
    }

}
