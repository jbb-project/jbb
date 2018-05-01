/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.faq;

import org.assertj.core.util.Lists;
import org.jbb.frontend.api.faq.Faq;
import org.jbb.frontend.api.faq.FaqCategory;
import org.jbb.frontend.api.faq.FaqEntry;
import org.jbb.frontend.api.faq.FaqException;
import org.jbb.frontend.api.faq.FaqService;
import org.jbb.frontend.impl.BaseIT;
import org.jbb.frontend.impl.faq.model.FaqCategoryEntity;
import org.jbb.frontend.impl.faq.model.FaqEntryEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class FaqServiceIT extends BaseIT {

    @Autowired
    private FaqService faqService;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullFaqPassed() throws Exception {
        // when
        faqService.setFaq(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = FaqException.class)
    public void shouldThrowFaqException_whenNullFaqCategoryName() throws Exception {
        // given
        Faq faq = exampleFaq();
        ((FaqCategoryEntity) faq.getCategories().get(0)).setName(null);

        // when
        faqService.setFaq(faq);

        // then
        // throw FaqException
    }

    @Test(expected = FaqException.class)
    public void shouldThrowFaqException_whenEmptyFaqCategoryName() throws Exception {
        // given
        Faq faq = exampleFaq();
        ((FaqCategoryEntity) faq.getCategories().get(0)).setName(EMPTY);

        // when
        faqService.setFaq(faq);

        // then
        // throw FaqException
    }

    @Test(expected = FaqException.class)
    public void shouldThrowFaqException_whenNullQuestion() throws Exception {
        // given
        Faq faq = exampleFaq();
        ((FaqEntryEntity) faq.getCategories().get(0).getQuestions().get(0)).setQuestion(null);

        // when
        faqService.setFaq(faq);

        // then
        // throw FaqException
    }

    @Test(expected = FaqException.class)
    public void shouldThrowFaqException_whenEmptyQuestion() throws Exception {
        // given
        Faq faq = exampleFaq();
        ((FaqEntryEntity) faq.getCategories().get(0).getQuestions().get(0)).setQuestion(EMPTY);

        // when
        faqService.setFaq(faq);

        // then
        // throw FaqException
    }

    @Test(expected = FaqException.class)
    public void shouldThrowFaqException_whenNullAnswer() throws Exception {
        // given
        Faq faq = exampleFaq();
        ((FaqEntryEntity) faq.getCategories().get(0).getQuestions().get(0)).setAnswer(null);

        // when
        faqService.setFaq(faq);

        // then
        // throw FaqException
    }

    @Test(expected = FaqException.class)
    public void shouldThrowFaqException_whenEmptyAnswer() throws Exception {
        // given
        Faq faq = exampleFaq();
        ((FaqEntryEntity) faq.getCategories().get(0).getQuestions().get(0)).setAnswer(EMPTY);

        // when
        faqService.setFaq(faq);

        // then
        // throw FaqException
    }

    @Test
    public void shouldSetAndGetFaq() throws Exception {
        // given
        Faq validFaq = exampleFaq();

        // when
        faqService.setFaq(validFaq);
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

    @Test
    public void shouldSupportLongQuestionsAndAnswers() throws Exception {
        // given
        Faq validFaq = exampleFaq();
        FaqEntry faqEntry = validFaq.getCategories().get(0).getQuestions().get(0);

        String longQuestion = "foo?????????????????????????????????????????????????????????????????"
                + "????????????????????????????????????????????????????????????????????????????????????"
                + "????????????????????????????????????????????????????????????????????????????????????"
                + "????????????????????????????????????????????????????????????????????????????????????"
                + "???????????????????????????????????????????????????????????????????????????????????";

        String longAnswer = "bar!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
                + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
                + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
                + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
                + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";

        // when
        ((FaqEntryEntity) faqEntry).setQuestion(longQuestion);
        ((FaqEntryEntity) faqEntry).setAnswer(longAnswer);

        faqService.setFaq(validFaq);
        Faq faq = faqService.getFaq();

        // then
        FaqEntry resultFaqEntry = faq.getCategories().get(0).getQuestions().get(0);
        assertThat(resultFaqEntry.getQuestion()).isEqualTo(longQuestion);
        assertThat(resultFaqEntry.getAnswer()).isEqualTo(longAnswer);
    }

    private Faq exampleFaq() {
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
                .entries(newArrayList(firstFaqEntry, secondFaqEntry))
                .position(1)
                .build();

        return Faq.builder().categories(Lists.newArrayList(firstCategory)).build();
    }
}