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
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.ext.spring.DelegatingLogbackAppender;

public class LogbackPreSpringStartConfigurator extends ContextAwareBase implements Configurator {

    @Override
    public void configure(LoggerContext loggerContext) {
        DelegatingLogbackAppender cachingAppender = DelegatingLogbackAppenderHolder.getInstance();
        cachingAppender.setContext(loggerContext);

        // configure root logger
        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.ALL);
        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(cachingAppender);

    }

}
