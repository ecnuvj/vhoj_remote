package com.vjudge.ecnuvj.remote.provider.codeforces;

import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.shared.codeforces.CFStyleLanguageFinder;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CodeForcesLanguageFinder extends CFStyleLanguageFinder {

    @Override
    public RemoteOjInfo getOjInfo() {
        return CodeForcesInfo.INFO;
    }

    @Override
    public HashMap<Integer, String> getLanguagesConverter() {
        return null;
    }
}
