package com.vjudge.ecnuvj.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartUpListener implements ServletContextListener {
    private final static Logger log = LoggerFactory.getLogger(StartUpListener.class);

    public void contextInitialized(ServletContextEvent event) {
        log.info("System startup");
        ApplicationContainer.serveletContext = event.getServletContext();
    }

    public void contextDestroyed(ServletContextEvent event) {
        log.info("System shutdown");
    }

}
