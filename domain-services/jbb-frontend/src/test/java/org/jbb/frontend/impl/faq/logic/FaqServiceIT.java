/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.faq.logic;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;
import org.assertj.core.util.Lists;
import org.jbb.frontend.api.faq.Faq;
import org.jbb.frontend.api.faq.FaqCategory;
import org.jbb.frontend.api.faq.FaqService;
import org.jbb.frontend.impl.FrontendConfig;
import org.jbb.frontend.impl.faq.model.FaqCategoryEntity;
import org.jbb.frontend.impl.faq.model.FaqEntryEntity;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CommonsConfig.class, MockCommonsConfig.class,
    FrontendConfig.class, PropertiesConfig.class,
    EventBusConfig.class, DbConfig.class})
public class FaqServiceIT {

    @Autowired
    private FaqService faqService;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullFaqPassed() throws Exception {
        // when
        faqService.setFaq(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldSetAndGetFaq() throws Exception {
        // given
        FaqEntryEntity firstFaqEntry = FaqEntryEntity.builder()
            .question("What is jBB?")
            .answer("jBB is a bulletin board software")
            .position(1)
            .build();

        FaqEntryEntity secondFaqEntry = FaqEntryEntity.builder()
            .question("How can I get support?")
            .answer("Visit https://github.com/jbb-project/jbb")
            .position(2)
            .build();

        FaqCategoryEntity firstCategory = FaqCategoryEntity.builder()
            .name("General")
            .entries(com.google.common.collect.Lists.newArrayList(firstFaqEntry, secondFaqEntry))
            .position(1)
            .build();

        // when
        faqService.setFaq(Faq.builder().categories(Lists.newArrayList(firstCategory)).build());
        Faq faq = faqService.getFaq();
        List<FaqCategory> faqCategories = faq.getCategories();

        // then
        assertThat(faqCategories).hasSize(1);
        assertThat(faqCategories.get(0).getName()).isEqualTo("General");
        assertThat(faqCategories.get(0).getQuestions()).hasSize(2);
        assertThat(faqCategories.get(0).getQuestions().get(0).getQuestion())
            .isEqualTo("What is jBB?");
        assertThat(faqCategories.get(0).getQuestions().get(0).getAnswer())
            .isEqualTo("jBB is a bulletin board software");
        assertThat(faqCategories.get(0).getQuestions().get(1).getQuestion())
            .isEqualTo("How can I get support?");
        assertThat(faqCategories.get(0).getQuestions().get(1).getAnswer())
            .isEqualTo("Visit https://github.com/jbb-project/jbb");
    }
}