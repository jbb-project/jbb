/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp;

import com.google.common.collect.Lists;

import org.jbb.frontend.api.acp.AcpStructure;
import org.jbb.frontend.impl.acp.dao.AcpCategoryRepository;
import org.jbb.frontend.impl.acp.model.AcpCategoryEntity;
import org.jbb.frontend.impl.acp.model.AcpElementEntity;
import org.jbb.frontend.impl.acp.model.AcpSubcategoryEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAcpServiceTest {
    @Mock
    private AcpCategoryRepository categoryRepositoryMock;

    @InjectMocks
    private DefaultAcpService acpService;

    @Test
    public void shouldReturnFullAcpStructure_directlyFromRepository() {
        // given
        AcpCategoryEntity firstCategory = AcpCategoryEntity.builder()
                .name("First category")
                .viewName("first")
                .ordering(1)
                .subcategories(Lists.newArrayList(AcpSubcategoryEntity.builder()
                        .name("First subcategory")
                        .ordering(1)
                        .elements(Lists.newArrayList(AcpElementEntity.builder()
                                .name("First element")
                                .viewName("element")
                                .ordering(1)
                                .build()))
                        .build()))
                .build();

        AcpCategoryEntity secondCategory = AcpCategoryEntity.builder()
                .name("Second category")
                .viewName("second")
                .ordering(2)
                .subcategories(Lists.newArrayList(AcpSubcategoryEntity.builder()
                        .name("First subcategory")
                        .ordering(1)
                        .elements(Lists.newArrayList(AcpElementEntity.builder()
                                .name("First element")
                                .viewName("element")
                                .ordering(1)
                                .build()))
                        .build()))
                .build();

        given(categoryRepositoryMock.findByOrderByOrdering()).willReturn(Lists.newArrayList(firstCategory, secondCategory));

        // when
        AcpStructure acpStructure = acpService.getAcpStructure();

        // then
        assertThat(acpStructure.getCategories()).hasSize(2);
        assertThat(acpStructure.getCategories().get(0).getName()).isEqualTo("First category");
        assertThat(acpStructure.getCategories().get(0).getViewName()).isEqualTo("first");
        assertThat(acpStructure.getCategories().get(1).getName()).isEqualTo("Second category");
        assertThat(acpStructure.getCategories().get(1).getViewName()).isEqualTo("second");
    }
}