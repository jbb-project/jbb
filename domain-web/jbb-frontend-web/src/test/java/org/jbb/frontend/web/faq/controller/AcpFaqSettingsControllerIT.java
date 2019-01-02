/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.faq.controller;


import com.google.common.collect.Sets;

import org.assertj.core.util.Lists;
import org.jbb.frontend.api.acp.AcpService;
import org.jbb.frontend.api.acp.AcpStructure;
import org.jbb.frontend.api.faq.Faq;
import org.jbb.frontend.api.faq.FaqCategory;
import org.jbb.frontend.api.faq.FaqEntry;
import org.jbb.frontend.api.faq.FaqException;
import org.jbb.frontend.api.faq.FaqService;
import org.jbb.frontend.web.BaseIT;
import org.jbb.lib.mvc.WildcardReloadableResourceBundleMessageSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Properties;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AcpFaqSettingsControllerIT extends BaseIT {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    FaqService faqServiceMock;

    @Autowired
    AcpService acpServiceMock;

    @Autowired
    WildcardReloadableResourceBundleMessageSource messageSource;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
        Mockito.reset(faqServiceMock, acpServiceMock);
    }

    @Test
    public void shouldUseHomeView_whenMainUrlInvoked() throws Exception {
        // given
        Faq faq = exampleFaq();
        given(faqServiceMock.getFaq()).willReturn(faq);
        prepareAcpService();

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/faq"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("defaultLayout"))
                .andExpect(model().attribute("contentViewName", "acpLayout"))
                .andExpect(model().attributeExists("faqSettingsForm"));
    }

    @Test
    public void shouldSetFlag_whenValidFaqPassed() throws Exception {
        // given
        Faq faq = exampleFaq();
        given(faqServiceMock.getFaq()).willReturn(faq);
        prepareAcpService();

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/faq")
                .param("categories[0].name", "general faq")
                .param("categories[0].entries[0].question", "quo vadis?")
                .param("categories[0].entries[0].answer", "nowhere"));

        result.andExpect(status().isOk())
                .andExpect(view().name("defaultLayout"))
                .andExpect(model().attribute("contentViewName", "acpLayout"))
                .andExpect(model().attributeExists("faqSettingsForm"))
                .andExpect(model().attribute("faqSettingsFormSaved", true));
    }

    @Test
    public void shouldNotSetFlag_whenValidFaqFailed() throws Exception {
        // given
        Properties prop = new Properties();
        prop.setProperty("message", "message");
        messageSource.setCommonMessages(prop);
        ConstraintViolation<?> constraintViolation = mock(ConstraintViolation.class);
        Path propertyPath = mock(Path.class);
        given(constraintViolation.getPropertyPath()).willReturn(propertyPath);
        given(constraintViolation.getMessage()).willReturn("message");
        given(propertyPath.toString()).willReturn("categories[0].questions[0].question");
        doThrow(new FaqException(Sets.newHashSet(constraintViolation))).when(faqServiceMock)
                .setFaq(any());
        prepareAcpService();

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/faq")
                .param("categories[0].name", "general faq")
                .param("categories[0].entries[0].question", "")
                .param("categories[0].entries[0].answer", "nowhere"));

        result.andExpect(status().isOk())
                .andExpect(view().name("defaultLayout"))
                .andExpect(model().attribute("contentViewName", "acpLayout"))
                .andExpect(model().attributeExists("faqSettingsForm"))
                .andExpect(model().attributeDoesNotExist("faqSettingsFormSaved"));
    }


    private void prepareAcpService() {
        AcpStructure.Category category = mock(AcpStructure.Category.class);
        given(category.getViewName()).willReturn("general");
        AcpStructure acpStructure = new AcpStructure.Builder().add(category).build();
        given(acpServiceMock.getAcpStructure()).willReturn(acpStructure);
    }

    private Faq exampleFaq() {
        FaqEntry firstFaqEntry = FaqEntry.builder()
                .answer("Foo?")
                .question("Bar!")
                .build();

        FaqEntry secondFaqEntry = FaqEntry.builder()
                .answer("Bar?")
                .question("Foo!")
                .build();

        FaqCategory category = FaqCategory.builder()
                .name("General")
                .questions(Lists.newArrayList(firstFaqEntry, secondFaqEntry))
                .build();

        return Faq.builder().categories(Lists.newArrayList(category)).build();
    }

}