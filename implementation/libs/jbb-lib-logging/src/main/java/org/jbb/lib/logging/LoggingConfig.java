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

import org.jbb.lib.core.JbbMetaData;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Appender;
import ch.qos.logback.ext.spring.ApplicationContextHolder;

@Configuration
@ComponentScan("org.jbb.lib.logging")
public class LoggingConfig {
    public static final String PROXY_APPENDER_BEAN_NAME = "aggregateAppenderProxyBean";

    @Bean
    public static LoggerContext loggerContext() {
        return (LoggerContext) StaticLoggerBinder.getSingleton().getLoggerFactory();
    }

    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

    @Bean(name = PROXY_APPENDER_BEAN_NAME, initMethod = "start", destroyMethod = "stop")
    public Appender aggregateProxyAppender(LoggerContext ctx) {
        AggregateProxyAppender appender = new AggregateProxyAppender();
        appender.setContext(ctx);
        return appender;
    }

    @Bean
    @DependsOn(PROXY_APPENDER_BEAN_NAME)
    public LoggingBootstrapper loggingBootstrapper(JbbMetaData jbbMetaData, LoggerContext ctx) {
        return new LoggingBootstrapper(jbbMetaData, ctx);
    }
}
