/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.controller;

import com.google.common.collect.Lists;

import org.jbb.board.api.model.Forum;
import org.jbb.board.api.model.ForumCategory;
import org.jbb.board.api.service.BoardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class HomePageControllerTest {
    @Mock
    private BoardService boardServiceMock;
    
    @InjectMocks
    private HomePageController homePageController;

    @Test
    public void shouldReturnHomePage_whenMainMethodInvoked() throws Exception {
        // given
        ForumCategory forumCategory = mock(ForumCategory.class);
        given(forumCategory.getName()).willReturn("category");
        given(forumCategory.getForums()).willReturn(Lists.newArrayList(mock(Forum.class)));

        given(boardServiceMock.getForumCategories()).willReturn(Lists.newArrayList(forumCategory));
        
        // when
        String viewName = homePageController.main(mock(Model.class));

        // then
        assertThat(viewName).isEqualTo("home");
    }

    @Test
    public void shouldReturnFaqPage_whenFaqMethodInvoked() throws Exception {
        // when
        String viewName = homePageController.faq();

        // then
        assertThat(viewName).isEqualTo("faq");
    }
}