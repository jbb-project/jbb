/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp;

import com.google.common.collect.Lists;

import org.jbb.frontend.api.acp.AcpCategory;
import org.jbb.frontend.api.acp.AcpElement;
import org.jbb.frontend.api.acp.AcpSubcategory;
import org.jbb.frontend.impl.acp.dao.AcpCategoryRepository;
import org.jbb.frontend.impl.acp.dao.AcpElementRepository;
import org.jbb.frontend.impl.acp.dao.AcpSubcategoryRepository;
import org.jbb.frontend.impl.acp.model.AcpCategoryEntity;
import org.jbb.frontend.impl.acp.model.AcpElementEntity;
import org.jbb.frontend.impl.acp.model.AcpSubcategoryEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAcpServiceTest {
    @Mock
    private AcpCategoryRepository categoryRepositoryMock;

    @Mock
    private AcpSubcategoryRepository subcategoryRepositoryMock;

    @Mock
    private AcpElementRepository elementRepositoryMock;

    @InjectMocks
    private DefaultAcpService acpService;

    @Test
    public void shouldReturnCategories_directlyFromRepository() throws Exception {
        // given
        AcpCategoryEntity firstCategory = mock(AcpCategoryEntity.class);
        AcpCategoryEntity seocndCategory = mock(AcpCategoryEntity.class);

        given(categoryRepositoryMock.findByOrderByOrdering()).willReturn(Lists.newArrayList(firstCategory, seocndCategory));

        // when
        List<AcpCategory> acpCategories = acpService.selectAllCategoriesOrdered();

        // then
        assertThat(acpCategories).hasSize(2);
        assertThat(acpCategories.get(0)).isEqualTo(firstCategory);
        assertThat(acpCategories.get(1)).isEqualTo(seocndCategory);
    }

    @Test
    public void shouldReturnSubcategoriesAndElementsInRightOrder_whenSelectingForGivenCategoryInvoked() throws Exception {
        // given
        AcpSubcategoryEntity firstSubcategory = mock(AcpSubcategoryEntity.class);
        given(firstSubcategory.getOrdering()).willReturn(1);

        AcpElementEntity firstElementOfFirstSubcategory = mock(AcpElementEntity.class);
        given(firstElementOfFirstSubcategory.getOrdering()).willReturn(1);
        AcpElementEntity secondElementOfFirstSubcategory = mock(AcpElementEntity.class);
        given(secondElementOfFirstSubcategory.getOrdering()).willReturn(2);
        AcpElementEntity thirdElementOfFirstSubcategory = mock(AcpElementEntity.class);
        given(thirdElementOfFirstSubcategory.getOrdering()).willReturn(3);

        given(firstSubcategory.getElements()).willReturn(
                Lists.newArrayList(
                        firstElementOfFirstSubcategory,
                        secondElementOfFirstSubcategory,
                        thirdElementOfFirstSubcategory
                )
        );


        AcpSubcategoryEntity secondSubcategory = mock(AcpSubcategoryEntity.class);
        given(secondSubcategory.getOrdering()).willReturn(2);

        AcpElementEntity firstElementOfSecondSubcategory = mock(AcpElementEntity.class);
        given(firstElementOfSecondSubcategory.getOrdering()).willReturn(1);
        AcpElementEntity secondElementOfSecondSubcategory = mock(AcpElementEntity.class);
        given(secondElementOfSecondSubcategory.getOrdering()).willReturn(2);
        AcpElementEntity thirdElementOfSecondSubcategory = mock(AcpElementEntity.class);
        given(thirdElementOfSecondSubcategory.getOrdering()).willReturn(3);

        given(secondSubcategory.getElements()).willReturn(
                Lists.newArrayList(
                        firstElementOfSecondSubcategory,
                        secondElementOfSecondSubcategory,
                        thirdElementOfSecondSubcategory
                )
        );

        String categoryName = "general";
        given(subcategoryRepositoryMock.findByCategoryOrderByOrdering(eq(categoryName)))
                .willReturn(Lists.newArrayList(firstSubcategory, secondSubcategory));

        // when
        NavigableMap<AcpSubcategory, Collection<AcpElement>> map = acpService
                .selectAllSubcategoriesAndElements(categoryName);

        // then
        assertThat(map.firstKey()).isEqualTo(firstSubcategory);
        assertThat(map.lastKey()).isEqualTo(secondSubcategory);
        assertThat(map.keySet()).hasSize(2);

        assertThat(map.get(firstSubcategory)).containsExactly(
                firstElementOfFirstSubcategory,
                secondElementOfFirstSubcategory,
                thirdElementOfFirstSubcategory
        );

        assertThat(map.get(secondSubcategory)).containsExactly(
                firstElementOfSecondSubcategory,
                secondElementOfSecondSubcategory,
                thirdElementOfSecondSubcategory
        );
    }

    @Test
    public void shouldReturnAcpCategory_forGivenCategoryViewName() throws Exception {
        // given
        String categoryViewName = "general";
        AcpCategoryEntity acpCategoryEntity = mock(AcpCategoryEntity.class);
        given(categoryRepositoryMock.findByViewName(eq(categoryViewName))).willReturn(acpCategoryEntity);

        // when
        AcpCategory result = acpService.selectCategory(categoryViewName);

        // then
        assertThat(result).isEqualTo(acpCategoryEntity);
    }

    @Test
    public void shouldReturnAcpElementForGivenCategoryViewName_andElementViewName() throws Exception {
        // given
        String categoryViewName = "general";
        String elementViewName = "logging";
        AcpElementEntity acpElementEntity = mock(AcpElementEntity.class);
        given(elementRepositoryMock.findByCategoryAndElementName(eq(categoryViewName), eq(elementViewName)))
                .willReturn(acpElementEntity);

        // when
        AcpElement result = acpService.selectElement(categoryViewName, elementViewName);

        // then
        assertThat(result).isEqualTo(acpElementEntity);
    }
}