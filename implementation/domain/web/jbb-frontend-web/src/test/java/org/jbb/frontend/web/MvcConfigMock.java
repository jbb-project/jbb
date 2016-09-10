/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web;

import org.jbb.frontend.api.service.BoardNameService;
import org.jbb.frontend.api.service.UcpService;
import org.jbb.frontend.api.service.stacktrace.StackTraceVisibilityUsersService;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MvcConfigMock {
    @Bean
    @Primary
    public BoardNameService boardNameService() {
        return Mockito.mock(BoardNameService.class);
    }

    @Bean
    @Primary
    public UcpService ucpService() {
        return Mockito.mock(UcpService.class);
    }

    @Bean
    @Primary
    public StackTraceVisibilityUsersService strackTraceVisibilityUsersService() {
        return Mockito.mock(StackTraceVisibilityUsersService.class);
    }

    @Bean
    @Primary
    public ModulePropertiesFactory modulePropertiesFactory() {
        return Mockito.mock(ModulePropertiesFactory.class);
    }
}
