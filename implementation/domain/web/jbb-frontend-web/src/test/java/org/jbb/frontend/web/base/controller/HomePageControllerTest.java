/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.controller;

import org.jbb.frontend.api.service.BoardNameService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HomePageControllerTest {
    @Mock
    private BoardNameService boardNameServiceMock;

    @InjectMocks
    private HomePageController homePageController;

    @Test
    public void shouldReturnHomePage_whenMainMethodInvoked() throws Exception {
        // when
        String viewName = homePageController.main();

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

    @Test
    public void shouldInvokeService_whenSetBoardNameMethodInvoked() throws Exception {
        // given
        String newBoardName = "Board 3000";

        // when
        homePageController.setBoardName(newBoardName);

        // then
        verify(boardNameServiceMock, times(1)).setBoardName(eq(newBoardName));
    }

    @Test
    public void shouldRedirectToMainPage_whenSetBoardNameMethodInvoked() throws Exception {
        // given
        String newBoardName = "Board 3000";

        // when
        String viewName = homePageController.setBoardName(newBoardName);

        // then
        assertThat(viewName).isEqualTo("redirect:/");
    }
}