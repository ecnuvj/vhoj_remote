package com.vjudge.ecnuvj.remote.provider.mxt;

import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.language.LanguageFinder;
import com.vjudge.ecnuvj.tool.Handler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created with IDEA
 * User: zzzz76
 * Date: 2020-11-22
 */
@Component
public class MXTLanguageFinder implements LanguageFinder {

    @Override
    public RemoteOjInfo getOjInfo() {
        return MXTInfo.INFO;
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
        languageList.put("0", "C");
        languageList.put("1", "C++");
        languageList.put("2", "Pascal");
        languageList.put("3", "Java");
        languageList.put("4", "Ruby");
        languageList.put("5", "Bash");
        languageList.put("6", "Python");
        languageList.put("7", "PHP");
        languageList.put("8", "Perl");
        languageList.put("9", "C#");
        languageList.put("10", "Obj-C");
        languageList.put("11", "FreeBasic");
        languageList.put("12", "Scheme");
        languageList.put("13", "Clang");
        languageList.put("14", "Clang++");
        languageList.put("15", "Lua");
        languageList.put("16", "JavaScript");
        languageList.put("17", "Go");
        languageList.put("18", "SQL(sqlite3)");
        languageList.put("19", "Fortran");
        languageList.put("20", "Matlab(Octave)");
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
        converter.put(1, "1");
        converter.put(2, "3");
        return converter;
    }
}

