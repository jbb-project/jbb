/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.logging;

import org.apache.commons.io.FileUtils;
import org.jbb.lib.core.JbbMetaData;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.ext.spring.EventCacheMode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogbackSpringConfigurator extends ContextAwareBase implements Configurator {
    public static final String JDBC_LOGGER_NAME = "jdbc";

    private JbbMetaData jbbMetaData;

    public void setJbbMetaData(JbbMetaData jbbMetaData) {
        this.jbbMetaData = jbbMetaData;
    }

    @Override
    public void configure(LoggerContext loggerContext) {
        AutocloseableDelegatingLogbackAppender delegatingLogbackAppender = new AutocloseableDelegatingLogbackAppender();
        delegatingLogbackAppender.setCacheMode(EventCacheMode.ON.toString());
        delegatingLogbackAppender.setBeanName(LoggingConfig.PROXY_APPENDER_BEAN_NAME);
        delegatingLogbackAppender.setContext(loggerContext);
        delegatingLogbackAppender.start();
        loggerContext.getLogger(JDBC_LOGGER_NAME).setLevel(Level.INFO);
        loggerContext.getLogger(JDBC_LOGGER_NAME).setAdditive(true);
        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.ALL);
        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(delegatingLogbackAppender);
    }

    @PostConstruct//FIXME refactor
    public void reconfigure() {
        try {
            File logDir = new File(jbbMetaData.jbbHomePath() + "/log");
            if (!logDir.exists()) {
                logDir.mkdir();
            }
            System.setProperty("jbb.log.dir", logDir.getAbsolutePath());
            String location = jbbMetaData.jbbHomePath() + "/logback-webapp.xml";
            File logbackConfigurationFile = new File(location);
            if (!logbackConfigurationFile.exists()) {
                ClassPathResource classPathResource = new ClassPathResource("default-logback.xml");
                FileUtils.copyURLToFile(classPathResource.getURL(), logbackConfigurationFile);
            }
            String resolvedLocation = SystemPropertyUtils.resolvePlaceholders(location);
            URL url = ResourceUtils.getURL(resolvedLocation);
            LoggerContext loggerContext = (LoggerContext) StaticLoggerBinder.getSingleton().getLoggerFactory();
            new ContextInitializer(loggerContext).configureByResource(url);
            log.info("Reconfiguration of logger finished");
        } catch (FileNotFoundException | JoranException e) {
            throw new IllegalStateException("Unexpected error during reading logging configuration", e);
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected error during reading default logging configuration file", e);
        }
    }
}
