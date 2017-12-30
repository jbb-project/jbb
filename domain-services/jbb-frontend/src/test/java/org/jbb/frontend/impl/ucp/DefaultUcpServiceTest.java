/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp;

import org.jbb.frontend.impl.ucp.dao.UcpCategoryRepository;
import org.jbb.frontend.impl.ucp.dao.UcpElementRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUcpServiceTest {
    @Mock
    private UcpCategoryRepository categoryRepositoryMock;

    @Mock
    private UcpElementRepository elementRepositoryMock;

    @InjectMocks
    private DefaultUcpService ucpService;

    @Test
    public void shouldFindByOrdering() throws Exception {
        // when
        ucpService.selectAllCategoriesOrdered();

        // then
        Mockito.verify(categoryRepositoryMock, times(1)).findByOrderByOrdering();
    }

    @Test
    public void shouldInvokedRepository_forOrderedElements() throws Exception {
        // when
        ucpService.selectAllElementsOrderedForCategoryViewName("overview");

        // then
        Mockito.verify(elementRepositoryMock, times(1)).findByCategoryNameOrderByOrdering(eq("overview"));
    }

    @Test
    public void shouldReturnCategoryWithGivenViewName() throws Exception {
        // when
        ucpService.selectCategoryForViewName("profile");

        // then
        Mockito.verify(categoryRepositoryMock, times(1)).findByViewName(eq("profile"));
    }

    @Test
    public void shouldReturnElementForGivenCategoryAndElementViewName() throws Exception {
        // when
        ucpService.selectElementForViewName("profile", "edit");

        // then
        Mockito.verify(elementRepositoryMock, times(1)).findByCategoryAndElementName(eq("profile"), eq("edit"));
    }
}