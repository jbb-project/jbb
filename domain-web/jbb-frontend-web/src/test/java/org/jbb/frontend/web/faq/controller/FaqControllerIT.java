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

import org.assertj.core.util.Lists;
import org.jbb.frontend.api.faq.Faq;
import org.jbb.frontend.api.faq.FaqCategory;
import org.jbb.frontend.api.faq.FaqEntry;
import org.jbb.frontend.api.faq.FaqService;
import org.jbb.frontend.web.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class FaqControllerIT extends BaseIT {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    FaqService faqService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void shouldUseFaqView_whenFaqUrlInvoked() throws Exception {
        // given
        Faq exampleFaq = exampleFaq();
        when(faqService.getFaq()).thenReturn(exampleFaq);

        // when
        ResultActions result = mockMvc.perform(get("/faq"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("defaultLayout"))
                .andExpect(model().attribute("contentViewName", "faq"));
    }

    private Faq exampleFaq() {
        FaqCategory category = mock(FaqCategory.class);
        when(category.getName()).thenReturn("Category name");

        FaqEntry entry = mock(FaqEntry.class);
        when(entry.getQuestion()).thenReturn("foo?");
        when(entry.getAnswer()).thenReturn("bar!");

        when(category.getQuestions()).thenReturn(Lists.newArrayList(entry));

        return Faq.builder().categories(Lists.newArrayList(category)).build();
    }

}