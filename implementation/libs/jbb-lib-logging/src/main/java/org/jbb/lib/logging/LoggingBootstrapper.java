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
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingBootstrapper {
    private static final String JBB_LOG_PATH_PROPERTY = "jbb.log.dir";
    private static final String LOG_DIR_NAME = "log";
    private static final String LOG_CONF_FILE_NAME = "logback-webapp.xml";
    private static final String CLASSPATH_DEFAULT_LOG_CONF_FILE_NAME = "default-logback.xml";

    private final JbbMetaData jbbMetaData;
    private final LoggerContext loggerContext;

    public LoggingBootstrapper(JbbMetaData jbbMetaData, LoggerContext loggerContext) {
        this.jbbMetaData = jbbMetaData;
        this.loggerContext = loggerContext;
    }

    @PostConstruct
    public void configure() {
        prepareLogDirectory();
        String location = prepareLogConfigurationFile();
        initLogging(location);
        log.info("Reconfiguration of logger finished");
    }

    private void prepareLogDirectory() {
        String logPathSystemProperty = System.getProperty(JBB_LOG_PATH_PROPERTY);

        String logDirPath;
        if (logPathSystemProperty != null) {
            logDirPath = logPathSystemProperty;
        } else {
            logDirPath = jbbMetaData.jbbHomePath() + File.separator + LOG_DIR_NAME;
        }

        File logDir = new File(logDirPath);
        if (!logDir.exists()) {
            logDir.mkdir();
        }
        System.setProperty("jbb.log.dir", logDir.getAbsolutePath());
    }

    private String prepareLogConfigurationFile() {
        String logConfFilePath = jbbMetaData.jbbHomePath() + File.separator + LOG_CONF_FILE_NAME;
        File logbackConfigurationFile = new File(logConfFilePath);
        if (!logbackConfigurationFile.exists()) {
            copyDefaultConfigurationToFile(logbackConfigurationFile);
        }
        return logConfFilePath;
    }

    private void copyDefaultConfigurationToFile(File logbackConfigurationFile) {
        ClassPathResource classPathResource = new ClassPathResource(CLASSPATH_DEFAULT_LOG_CONF_FILE_NAME);
        try {
            FileUtils.copyURLToFile(classPathResource.getURL(), logbackConfigurationFile);
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected error during reading default logging configuration file", e);
        }
    }

    private void initLogging(String logConfFile) {
        try {
            URL url = ResourceUtils.getURL(logConfFile);
            new ContextInitializer(loggerContext).configureByResource(url);
        } catch (FileNotFoundException | JoranException e) {
            throw new IllegalStateException("Unexpected error during init logging configuration", e);
        }
    }
}
