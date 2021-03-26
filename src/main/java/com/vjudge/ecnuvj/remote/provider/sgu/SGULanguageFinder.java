package com.vjudge.ecnuvj.remote.provider.sgu;

import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.language.LanguageFinder;
import com.vjudge.ecnuvj.tool.Handler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Component
public class SGULanguageFinder implements LanguageFinder {

    @Override
    public RemoteOjInfo getOjInfo() {
        return SGUInfo.INFO;
    }

    @Override
    public boolean isDiverse() {
        return false;
    }

    @Override
    public void getLanguages(String remoteProblemId, Handler<LinkedHashMap<String, String>> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public LinkedHashMap<String, String> getDefaultLanguages() {
        LinkedHashMap<String, String> languageList = new LinkedHashMap<String, String>();
        languageList.put("GNU C (MinGW, GCC 5)", "GNU C (MinGW, GCC 5)");
        languageList.put("GNU CPP (MinGW, GCC 5)", "GNU CPP (MinGW, GCC 5)");
        languageList.put("GNU CPP 14 (MinGW, GCC 5)", "GNU CPP 14 (MinGW, GCC 5)");
        languageList.put("Visual Studio C++ 2010", "Visual Studio C++ 2010");
        languageList.put("C#", "C#");
        languageList.put("Visual Studio C 2010", "Visual Studio C 2010");
        languageList.put("JAVA 7", "JAVA 7");
        languageList.put("Delphi 7.0", "Delphi 7.0");
        return languageList;
    }

    @Override
    public HashMap<String, String> getLanguagesAdapter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HashMap<Integer, String> getLanguagesConverter() {
        return null;
    }

}
