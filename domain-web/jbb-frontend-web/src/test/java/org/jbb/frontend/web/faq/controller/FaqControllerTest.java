/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.faq.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FaqControllerTest {

    @InjectMocks
    private FaqController faqController;

    @Test
    public void shouldReturnFaqPage_whenFaqMethodInvoked() throws Exception {
        // when
        String viewName = faqController.getFaq();

        // then
        assertThat(viewName).isEqualTo("faq");
    }

}