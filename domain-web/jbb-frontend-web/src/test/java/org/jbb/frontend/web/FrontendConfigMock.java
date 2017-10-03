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

import static org.mockito.Mockito.when;

import org.jbb.board.api.base.BoardSettings;
import org.jbb.board.api.base.BoardSettingsService;
import org.jbb.board.api.forum.BoardService;
import org.jbb.frontend.api.acp.AcpService;
import org.jbb.frontend.api.faq.FaqService;
import org.jbb.frontend.api.ucp.UcpService;
import org.jbb.system.api.install.InstallationService;
import org.jbb.system.api.stacktrace.StackTraceService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.ModelAndView;

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
    public BoardService boardService() {
        return Mockito.mock(BoardService.class);
    }

    @Bean
    @Primary
    public FaqService faqService() {
        return Mockito.mock(FaqService.class);
    }

    @Bean
    @Primary
    public InstallationService installationService() {
        InstallationService installationService = Mockito.mock(InstallationService.class);
        when(installationService.isInstalled()).thenReturn(true);
        return installationService;
    }

    @Bean
    @Primary
    public ModelAndView modelAndView() {
        return Mockito.mock(ModelAndView.class);
    }
}
