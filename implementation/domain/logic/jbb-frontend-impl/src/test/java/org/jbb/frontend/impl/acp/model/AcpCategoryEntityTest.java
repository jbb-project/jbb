/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp.model;

import com.google.common.collect.Lists;

import org.junit.Test;
import org.meanbean.test.BeanTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AcpCategoryEntityTest {

    @Test
    public void pojoTest() throws Exception {
        BeanTester beanTester = new BeanTester();
        beanTester.setIterations(3);
        beanTester.getFactoryCollection().addFactory(LocalDateTime.class, () -> LocalDateTime.now());

        beanTester.testBean(AcpCategoryEntity.class);
    }

    @Test
    public void builderTest() throws Exception {
        // when
        AcpCategoryEntity acpCategoryEntity = AcpCategoryEntity.builder()
                .name("foo")
                .viewName("bar")
                .ordering(1)
                .subcategories(Lists.newArrayList())
                .build();

        // then
        assertThat(acpCategoryEntity).isNotNull();
    }

}