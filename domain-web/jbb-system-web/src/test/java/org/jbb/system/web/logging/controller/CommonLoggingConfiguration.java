/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.logging.controller;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.RandomStringUtils;
import org.jbb.system.api.logging.model.AppLogger;
import org.jbb.system.api.logging.model.LogConsoleAppender;
import org.jbb.system.api.logging.model.LogFileAppender;
import org.jbb.system.api.logging.model.LogLevel;
import org.jbb.system.api.logging.model.LogLevelFilter;
import org.jbb.system.api.logging.model.LogThresholdFilter;

public final class CommonLoggingConfiguration {

    public static LogConsoleAppender correctConsoleAppender() {
        LogConsoleAppender consoleAppender = new LogConsoleAppender();
        consoleAppender.setName("consoleAppender");
        consoleAppender.setTarget(LogConsoleAppender.Target.SYSTEM_OUT);
        consoleAppender.setPattern("[%d{dd-MM-yyyy HH:mm:ss}] [%thread] %-5level --&gt; [%c] %m%n");
        consoleAppender.setFilter(new LogThresholdFilter(LogLevel.DEBUG));
        return consoleAppender;
    }

    public static LogFileAppender correctFileAppender() {
        LogFileAppender fileAppender = new LogFileAppender();
        fileAppender.setName("fileAppender");
        fileAppender.setCurrentLogFileName("jbb.log");
        fileAppender.setRotationFileNamePattern("jbb_%d{yyyy-MM-dd}_%i.log");
        fileAppender.setMaxFileSize(LogFileAppender.FileSize.valueOf("100 MB"));
        fileAppender.setMaxHistory(7);
        fileAppender.setFilter(new LogLevelFilter(LogLevel.INFO));
        fileAppender.setPattern("[%d{dd-MM-yyyy HH:mm:ss}] [%thread] %-5level --&gt; [%c] %m%n");
        return fileAppender;
    }

    public static AppLogger correctAppLogger() {
        AppLogger appLogger = new AppLogger();
        appLogger.setName("org.jbb.testing");
        appLogger.setLevel(LogLevel.ERROR);
        appLogger.setAddivity(false);

        LogFileAppender fileAppender = correctFileAppender();
        fileAppender.setName(RandomStringUtils.randomAlphanumeric(20));

        appLogger.setAppenders(Lists.newArrayList(fileAppender));

        return appLogger;
    }
}
