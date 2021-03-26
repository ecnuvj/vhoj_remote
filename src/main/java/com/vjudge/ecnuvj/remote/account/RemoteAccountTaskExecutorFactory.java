package com.vjudge.ecnuvj.remote.account;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.vjudge.ecnuvj.remote.account.config.RemoteAccountOJConfig;
import com.vjudge.ecnuvj.remote.common.RemoteOj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * @author tcg
 */
@Component
public class RemoteAccountTaskExecutorFactory {
    private final static Logger log = LoggerFactory.getLogger(RemoteAccountTaskExecutorFactory.class);

    public File jsonConfig;

    @Autowired
    public RemoteAccountTaskExecutorFactory(@Value("${remote.accounts.path}") String jsonConfigPath) {
        this.jsonConfig = new File(RemoteAccountTaskExecutorFactory.class.getResource("/").getPath() + jsonConfigPath);
    }

    @Bean(initMethod = "init")
    public RemoteAccountTaskExecutor create() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
        Type type = new TypeToken<HashMap<RemoteOj, RemoteAccountOJConfig>>() {
        }.getType();
        HashMap<RemoteOj, RemoteAccountOJConfig> map = new Gson().fromJson(new FileReader(jsonConfig), type);
        if (map.containsKey(null)) {
            log.error("Remote OJ account config contains unknown OJ name");
            System.exit(-1);
        }
        return new RemoteAccountTaskExecutor(map);
    }

}
