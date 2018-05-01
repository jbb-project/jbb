/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.web;

import org.jbb.board.api.base.BoardSettingsService;
import org.jbb.board.api.forum.BoardService;
import org.jbb.board.api.forum.ForumCategoryService;
import org.jbb.board.api.forum.ForumService;
import org.jbb.frontend.api.format.FormatSettingsService;
import org.jbb.permissions.api.PermissionService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.validation.Validator;

@Configuration
public class BoardConfigMock {
    @Bean
    @Primary
    public BoardSettingsService boardSettingsService() {
        return Mockito.mock(BoardSettingsService.class);
    }

    @Bean
    @Primary
    public BoardService boardService() {
        return Mockito.mock(BoardService.class);
    }

    @Bean
    @Primary
    public ForumCategoryService forumCategoryService() {
        return Mockito.mock(ForumCategoryService.class);
    }

    @Bean
    @Primary
    public ForumService forumService() {
        return Mockito.mock(ForumService.class);
    }

    @Bean
    @Primary
    public PermissionService permissionService() {
        return Mockito.mock(PermissionService.class);
    }

    @Bean
    @Primary
    public FormatSettingsService formatSettingsService() {
        return Mockito.mock(FormatSettingsService.class);
    }

    @Bean
    @Primary
    public Validator validator() {
        return Mockito.mock(Validator.class);
    }
}
