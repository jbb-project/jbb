/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.ext.spring.DelegatingLogbackAppender;
import org.apache.commons.lang3.StringUtils;

public class LogbackPreSpringStartConfigurator extends ContextAwareBase implements Configurator {
    public static final String JBB_DEBUG_ENV_KEY = "JBB_DEBUG";

    @Override
    public void configure(LoggerContext loggerContext) {
        DelegatingLogbackAppender cachingAppender = DelegatingLogbackAppenderHolder.getInstance();
        cachingAppender.setContext(loggerContext);

        // configure root logger
        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.ALL);
        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(cachingAppender);

        prepareConsoleDebugAppenderIfApplicable(loggerContext);
    }

    // TODO it can be done better ;)
    private void prepareConsoleDebugAppenderIfApplicable(LoggerContext loggerContext) {
        String jbbDebugEnv = System.getenv(JBB_DEBUG_ENV_KEY);
        if (StringUtils.isNotBlank(jbbDebugEnv) && "true".equals(jbbDebugEnv)) {
            ConsoleAppender<ILoggingEvent> devConsoleAppender = new ConsoleAppender<>();
            devConsoleAppender.setContext(loggerContext);
            PatternLayoutEncoder encoder = new PatternLayoutEncoder();
            encoder.setContext(loggerContext);
            encoder.setPattern("[%d{dd-MM-yyyy HH:mm:ss}] [%thread] %-5level --> [%c] %m%n");
            encoder.start();
            devConsoleAppender.setEncoder(encoder);
            devConsoleAppender.start();

            loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(devConsoleAppender);
        }
    }
}
