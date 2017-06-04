/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.logic.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RedirectReplacingViewStrategyTest {
    @Mock
    private ModelAndView modelAndViewMock;

    private RedirectReplacingViewStrategy redirectReplacingViewStrategy;

    @Before
    public void setUp() throws Exception {
        redirectReplacingViewStrategy = new RedirectReplacingViewStrategy();
    }

    @Test
    public void shouldHandle_whenViewNameStartsWithRedirect() throws Exception {
        // given
        given(modelAndViewMock.getViewName()).willReturn("redirect:/faq");

        // when
        boolean canHandle = redirectReplacingViewStrategy.canHandle(modelAndViewMock);

        // then
        assertThat(canHandle).isTrue();
    }

    @Test
    public void shouldNotHandle_whenViewNameNotStartWithRedirect() throws Exception {
        // given
        given(modelAndViewMock.getViewName()).willReturn("members");

        // when
        boolean canHandle = redirectReplacingViewStrategy.canHandle(modelAndViewMock);

        // then
        assertThat(canHandle).isFalse();
    }
}