package com.vjudge.ecnuvj.remote.manager;

import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.language.LanguageFinder;
import com.vjudge.ecnuvj.remote.language.LanguageFindersHolder;
import com.vjudge.ecnuvj.tool.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 14861
 */
@Component
public class LanguageManager {
    private final static Logger log = LoggerFactory.getLogger(LanguageManager.class);
    private Map<RemoteOj, LinkedHashMap<String, String>> ojLanguages = new HashMap<>();
    private Map<RemoteOj, HashMap<Integer, String>> ojLanguagesConverter = new HashMap<>();

    public LinkedHashMap<String, String> getLanguages(RemoteOj remoteOj, String remoteProblemId) {
        if (!ojLanguages.containsKey(remoteOj)) {
            LanguageFinder finder = LanguageFindersHolder.getLanguageFinder(remoteOj);
            if (finder.isDiverse()) {
                final LinkedHashMap<String, String> map = new LinkedHashMap<>();
                finder.getLanguages(remoteProblemId, new Handler<LinkedHashMap<String, String>>() {
                    @Override
                    public void onError(Throwable t) {
                        log.error(t.getMessage(), t);
                    }

                    @Override
                    public void handle(LinkedHashMap<String, String> v) {
                        if (v != null) {
                            map.putAll(v);
                        } else {
                            map.put("", "unknown, please contact the admin to update LanguageFinder");
                        }
                    }
                });
                return map;
            }
            ojLanguages.put(remoteOj, finder.getDefaultLanguages());
        }
        return ojLanguages.get(remoteOj);
    }

    public LinkedHashMap<String, String> getLanguages(String remoteOj, String remoteProblemId) {
        RemoteOj oj = RemoteOj.valueOf(remoteOj);
        return getLanguages(oj, remoteProblemId);
    }

    public HashMap<Integer, String> getLanguagesConverter(String remoteOj) {
        RemoteOj oj = RemoteOj.valueOf(remoteOj);
        if (!ojLanguagesConverter.containsKey(oj)) {
            LanguageFinder finder = LanguageFindersHolder.getLanguageFinder(oj);
            ojLanguagesConverter.put(oj, finder.getLanguagesConverter());
        }
        return ojLanguagesConverter.get(oj);
    }
}
