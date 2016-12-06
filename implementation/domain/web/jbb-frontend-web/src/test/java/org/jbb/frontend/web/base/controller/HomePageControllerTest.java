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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class HomePageControllerTest {
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
}