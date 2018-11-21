/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Appender;
import ch.qos.logback.ext.spring.ApplicationContextHolder;
import io.micrometer.core.instrument.binder.logging.LogbackMetrics;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.commons.JbbMetaData;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@Import(CommonsConfig.class)
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
    public LoggingBootstrapper loggingBootstrapper(JbbMetaData jbbMetaData) {
        return new LoggingBootstrapper(jbbMetaData, loggerContext(), applicationContextHolder());
    }

    @Bean
    public ConfigurationRepository configurationRepository(LoggingBootstrapper loggingBootstrapper) {
        return new ConfigurationRepository(loggingBootstrapper);
    }

    @Bean(destroyMethod = "close")
    public LogbackMetrics logbackMetrics() {
        return new LogbackMetrics();
    }
}
