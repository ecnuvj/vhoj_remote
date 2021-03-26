package com.vjudge.ecnuvj.remote.language;

import com.vjudge.ecnuvj.remote.common.RemoteOjAware;
import com.vjudge.ecnuvj.tool.Handler;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Find eligible submitting languages for specified problem<br>
 * Implementation should be stateless.
 *
 * @author Isun
 */
public interface LanguageFinder extends RemoteOjAware {

    /**
     * Different languages for different problems
     *
     * @return
     */
    boolean isDiverse();

    /**
     * {submitted value} -> {displayed value}
     * May be called dynamically to update.
     *
     * @param remoteProblemId
     * @return
     */
    void getLanguages(String remoteProblemId, Handler<LinkedHashMap<String, String>> handler);

    /**
     * Should return immediately.
     *
     * @return {languageCode -> languageDescription}
     */
    LinkedHashMap<String, String> getDefaultLanguages();

    /**
     * Some old language code are not supported now. Necessary to find replacement for them.
     *
     * @return {OldLanguageCode -> NewLanguageCode}
     */
    HashMap<String, String> getLanguagesAdapter();

    /**
     * convert unified language code to ojs real code
     *
     * @return
     */
    HashMap<Integer, String> getLanguagesConverter();

}
