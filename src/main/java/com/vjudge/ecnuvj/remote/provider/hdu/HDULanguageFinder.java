package com.vjudge.ecnuvj.remote.provider.hdu;

import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.language.LanguageFinder;
import com.vjudge.ecnuvj.tool.Handler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Component
public class HDULanguageFinder implements LanguageFinder {

    @Override
    public RemoteOjInfo getOjInfo() {
        return HDUInfo.INFO;
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
        languageList.put("0", "G++");
        languageList.put("1", "GCC");
        languageList.put("2", "C++");
        languageList.put("3", "C");
        languageList.put("4", "Pascal");
        languageList.put("5", "Java");
        languageList.put("6", "C#");
        return languageList;
    }

    @Override
    public HashMap<String, String> getLanguagesAdapter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HashMap<Integer, String> getLanguagesConverter() {
        HashMap<Integer, String> converter = new HashMap<>(2);
        converter.put(1, "0");
        converter.put(2, "5");
        return converter;
    }

}
