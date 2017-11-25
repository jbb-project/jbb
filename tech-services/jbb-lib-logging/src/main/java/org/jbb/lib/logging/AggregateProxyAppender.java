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


import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

class AggregateProxyAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        Logger logger = ((LoggerContext) getContext()).getLogger(iLoggingEvent.getLoggerName());
        if (logger.isEnabledFor(iLoggingEvent.getLevel())) {
            logger.callAppenders(iLoggingEvent);
        }
    }
}
