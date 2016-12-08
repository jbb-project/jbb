/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web;

import org.jbb.system.api.service.DatabaseSettingsService;
import org.jbb.system.api.service.LoggingSettingsService;
import org.jbb.system.api.service.StackTraceService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SystemConfigMock {

    @Bean
    @Primary
    public StackTraceService stackTraceService() {
        return Mockito.mock(StackTraceService.class);
    }

    @Bean
    @Primary
    public LoggingSettingsService loggingSettingsService() {
        return Mockito.mock(LoggingSettingsService.class);
    }

    @Bean
    @Primary
    public DatabaseSettingsService databaseSettingsService() {
        return Mockito.mock(DatabaseSettingsService.class);
    }

}
