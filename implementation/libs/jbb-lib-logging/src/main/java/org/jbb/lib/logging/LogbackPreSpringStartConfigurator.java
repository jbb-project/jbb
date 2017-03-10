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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.ext.spring.EventCacheMode;

public class LogbackPreSpringStartConfigurator extends ContextAwareBase implements Configurator {
    public static final String JDBC_LOGGER_NAME = "jdbc";

    @Override
    public void configure(LoggerContext loggerContext) {
        CachingAppender cachingAppender = getCachingAppender(loggerContext);

        // configure logger for log4jdbc logs
        loggerContext.getLogger(JDBC_LOGGER_NAME).setLevel(Level.INFO);
        loggerContext.getLogger(JDBC_LOGGER_NAME).setAdditive(true);

        // configure root logger
        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.ALL);
        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(cachingAppender);
    }

    private CachingAppender getCachingAppender(LoggerContext loggerContext) {
        CachingAppender delegatingLogbackAppender = new CachingAppender();
        delegatingLogbackAppender.setCacheMode(EventCacheMode.ON);
        delegatingLogbackAppender.setBeanName(LoggingConfig.PROXY_APPENDER_BEAN_NAME);
        delegatingLogbackAppender.setContext(loggerContext);
        delegatingLogbackAppender.start();
        return delegatingLogbackAppender;
    }
}
