/*
 * Copyright (C) 2019 the original author or authors.
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
import org.jbb.frontend.api.faq.FaqException;
import org.jbb.frontend.api.faq.FaqService;
import org.jbb.frontend.impl.BaseIT;
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
    public void shouldThrowNPE_whenNullFaqPassed() {
        // when
        faqService.setFaq(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = FaqException.class)
    public void shouldThrowFaqException_whenNullFaqCategoryName() {
        // given
        Faq faq = exampleFaq();
        faq.getCategories().get(0).setName(null);

        // when
        faqService.setFaq(faq);

        // then
        // throw FaqException
    }

    @Test(expected = FaqException.class)
    public void shouldThrowFaqException_whenEmptyFaqCategoryName() {
        // given
        Faq faq = exampleFaq();
        faq.getCategories().get(0).setName(EMPTY);

        // when
        faqService.setFaq(faq);

        // then
        // throw FaqException
    }

    @Test(expected = FaqException.class)
    public void shouldThrowFaqException_whenNullQuestion() {
        // given
        Faq faq = exampleFaq();
        faq.getCategories().get(0).getQuestions().get(0).setQuestion(null);

        // when
        faqService.setFaq(faq);

        // then
        // throw FaqException
    }

    @Test(expected = FaqException.class)
    public void shouldThrowFaqException_whenEmptyQuestion() {
        // given
        Faq faq = exampleFaq();
        faq.getCategories().get(0).getQuestions().get(0).setQuestion(EMPTY);

        // when
        faqService.setFaq(faq);

        // then
        // throw FaqException
    }

    @Test(expected = FaqException.class)
    public void shouldThrowFaqException_whenNullAnswer() {
        // given
        Faq faq = exampleFaq();
        faq.getCategories().get(0).getQuestions().get(0).setAnswer(null);

        // when
        faqService.setFaq(faq);

        // then
        // throw FaqException
    }

    @Test(expected = FaqException.class)
    public void shouldThrowFaqException_whenEmptyAnswer() {
        // given
        Faq faq = exampleFaq();
        faq.getCategories().get(0).getQuestions().get(0).setAnswer(EMPTY);

        // when
        faqService.setFaq(faq);

        // then
        // throw FaqException
    }

    @Test
    public void shouldSetAndGetFaq() {
        // given
        Faq validFaq = exampleFaq();

        // when
        faqService.setFaq(validFaq);
        Faq faq = faqService.getFaq();
        List<Faq.Category> faqCategories = faq.getCategories();

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
    public void shouldSupportLongQuestionsAndAnswers() {
        // given
        Faq validFaq = exampleFaq();
        Faq.Entry faqEntry = validFaq.getCategories().get(0).getQuestions().get(0);

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
        faqEntry.setQuestion(longQuestion);
        faqEntry.setAnswer(longAnswer);

        faqService.setFaq(validFaq);
        Faq faq = faqService.getFaq();

        // then
        Faq.Entry resultFaqEntry = faq.getCategories().get(0).getQuestions().get(0);
        assertThat(resultFaqEntry.getQuestion()).isEqualTo(longQuestion);
        assertThat(resultFaqEntry.getAnswer()).isEqualTo(longAnswer);
    }

    private Faq exampleFaq() {
        Faq.Entry firstFaqEntry = Faq.Entry.builder()
                .question("What is jBB?")
                .answer("jBB is a bulletin board software")
                .build();

        Faq.Entry secondFaqEntry = Faq.Entry.builder()
                .question("How can I get support?")
                .answer("Visit https://github.com/jbb-project/jbb")
                .build();

        Faq.Category firstCategory = Faq.Category.builder()
                .name("General")
                .questions(newArrayList(firstFaqEntry, secondFaqEntry))
                .build();

        return Faq.builder().categories(Lists.newArrayList(firstCategory)).build();
    }
}