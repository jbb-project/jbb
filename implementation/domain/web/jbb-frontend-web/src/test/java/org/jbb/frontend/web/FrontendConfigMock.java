/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web;

import org.jbb.board.api.model.BoardSettings;
import org.jbb.board.api.service.BoardSettingsService;
import org.jbb.frontend.api.acp.AcpService;
import org.jbb.frontend.api.ucp.UcpService;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.jbb.system.api.service.StackTraceService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.ModelAndView;

import static org.mockito.Mockito.when;

@Configuration
public class FrontendConfigMock {
    @Bean
    @Primary
    public BoardSettingsService boardNameService() {
        BoardSettingsService boardSettingsService = Mockito.mock(BoardSettingsService.class);
        when(boardSettingsService.getBoardSettings()).thenReturn(boardSettings());
        return boardSettingsService;
    }

    @Bean
    @Primary
    public BoardSettings boardSettings() {
        return Mockito.mock(BoardSettings.class);
    }

    @Bean
    @Primary
    public UcpService ucpService() {
        return Mockito.mock(UcpService.class);
    }

    @Bean
    @Primary
    public StackTraceService stackTraceService() {
        return Mockito.mock(StackTraceService.class);
    }

    @Bean
    @Primary
    public AcpService acpService() {
        return Mockito.mock(AcpService.class);
    }

    @Bean
    @Primary
    public ModulePropertiesFactory modulePropertiesFactory() {
        return Mockito.mock(ModulePropertiesFactory.class);
    }

    @Bean
    @Primary
    public ModelAndView modelAndView() {
        return Mockito.mock(ModelAndView.class);
    }
}
