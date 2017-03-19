/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.faq.model;

import org.junit.Test;
import org.meanbean.test.BeanTester;

import static org.assertj.core.api.Assertions.assertThat;

public class FaqQuestionAnswerEntityTest {

    @Test
    public void pojoTest() throws Exception {
        BeanTester beanTester = new BeanTester();
        beanTester.setIterations(3);

        beanTester.testBean(FaqQuestionAnswerEntity.class);
    }

    @Test
    public void builderTest() throws Exception {
        // when
        FaqQuestionAnswerEntity faqQuestionAnswerEntity = FaqQuestionAnswerEntity.builder()
                .category(FaqCategoryEntity.builder().build())
                .question("What is life?")
                .answer("42")
                .position(1)
                .build();

        // then
        assertThat(faqQuestionAnswerEntity).isNotNull();
    }

}