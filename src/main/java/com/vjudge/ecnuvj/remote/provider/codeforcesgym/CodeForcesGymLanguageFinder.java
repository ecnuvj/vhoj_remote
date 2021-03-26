package com.vjudge.ecnuvj.remote.provider.codeforcesgym;

import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.shared.codeforces.CFStyleLanguageFinder;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CodeForcesGymLanguageFinder extends CFStyleLanguageFinder {

    @Override
    public RemoteOjInfo getOjInfo() {
        return CodeForcesGymInfo.INFO;
    }

    @Override
    public HashMap<Integer, String> getLanguagesConverter() {
        return null;
    }
}
