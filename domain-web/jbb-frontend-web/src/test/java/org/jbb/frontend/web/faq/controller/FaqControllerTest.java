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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.assertj.core.util.Lists;
import org.jbb.frontend.api.faq.Faq;
import org.jbb.frontend.api.faq.FaqCategory;
import org.jbb.frontend.api.faq.FaqEntry;
import org.jbb.frontend.api.faq.FaqService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

@RunWith(MockitoJUnitRunner.class)
public class FaqControllerTest {

    @Mock
    private FaqService faqServiceMock;

    @InjectMocks
    private FaqController faqController;

    @Test
    public void shouldReturnFaqPage_whenFaqMethodInvoked() throws Exception {
        // given
        Faq exampleFaq = exampleFaq();
        when(faqServiceMock.getFaq()).thenReturn(exampleFaq);

        // when
        String viewName = faqController.getFaq(mock(Model.class));

        // then
        assertThat(viewName).isEqualTo("faq");
    }

    private Faq exampleFaq() {
        FaqCategory category = mock(FaqCategory.class);
        when(category.getName()).thenReturn("Questions!");

        FaqEntry entry = mock(FaqEntry.class);
        when(entry.getQuestion()).thenReturn("foo?");
        when(entry.getAnswer()).thenReturn("bar!");

        when(category.getQuestions()).thenReturn(Lists.newArrayList(entry));

        return Faq.builder().faqCategories(Lists.newArrayList(category)).build();
    }

}