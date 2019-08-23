/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp;

import com.google.common.collect.Lists;

import org.jbb.frontend.api.ucp.UcpStructure;
import org.jbb.frontend.impl.ucp.dao.UcpCategoryRepository;
import org.jbb.frontend.impl.ucp.model.UcpCategoryEntity;
import org.jbb.frontend.impl.ucp.model.UcpElementEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUcpServiceTest {
    @Mock
    private UcpCategoryRepository categoryRepositoryMock;

    @InjectMocks
    private DefaultUcpService ucpService;

    @Test
    public void shouldReturnFullUcpStructure_directlyFromRepository() {
        // given
        UcpCategoryEntity firstCategory = UcpCategoryEntity.builder()
                .name("First category").viewName("first")
                .ordering(1)
                .elements(Lists.newArrayList(UcpElementEntity.builder()
                        .name("First element")
                        .viewName("element")
                        .ordering(1)
                        .build()))
                .build();
        UcpCategoryEntity secondCategory = UcpCategoryEntity.builder()
                .name("Second category").viewName("second")
                .ordering(2)
                .elements(Lists.newArrayList(UcpElementEntity.builder()
                        .name("First element")
                        .viewName("element")
                        .ordering(1)
                        .build()))
                .build();

        given(categoryRepositoryMock.findByOrderByOrdering())
                .willReturn(Lists.newArrayList(firstCategory, secondCategory));

        // when
        UcpStructure ucpStructure = ucpService.getUcpStructure();

        // then
        assertThat(ucpStructure.getCategories()).hasSize(2);
        assertThat(ucpStructure.getCategories().get(0).getName()).isEqualTo("First category");
        assertThat(ucpStructure.getCategories().get(0).getViewName()).isEqualTo("first");
        assertThat(ucpStructure.getCategories().get(1).getName()).isEqualTo("Second category");
        assertThat(ucpStructure.getCategories().get(1).getViewName()).isEqualTo("second");
    }

}