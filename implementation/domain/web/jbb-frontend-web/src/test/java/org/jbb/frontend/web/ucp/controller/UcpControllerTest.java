/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.ucp.controller;

import com.google.common.collect.Lists;

import org.jbb.frontend.api.model.UcpCategory;
import org.jbb.frontend.api.model.UcpElement;
import org.jbb.frontend.api.service.UcpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class UcpControllerTest {
    @Mock
    private UcpService ucpServiceMock;

    @InjectMocks
    private UcpController ucpController;

    @Test
    public void shouldRedirectToSubpageWithFirstCategoryName_whenUcpPathInvoked() throws Exception {
        // given
        UcpCategory firstCategory = mock(UcpCategory.class);
        given(firstCategory.getViewName()).willReturn("foo");

        UcpCategory secondCategory = mock(UcpCategory.class);

        given(ucpServiceMock.selectAllCategoriesOrdered()).willReturn(Lists.newArrayList(firstCategory, secondCategory));

        // when
        String viewName = ucpController.ucpMain();

        // then
        assertThat(viewName).isEqualTo("redirect:/ucp/foo");
    }

    @Test
    public void shouldRedirectToSubsubpageWithFirstElementName_whenUcpSubpageCategoryPathInvoked() throws Exception {
        // given
        UcpElement firstElement = mock(UcpElement.class);
        given(firstElement.getViewName()).willReturn("bar");

        UcpElement secondElement = mock(UcpElement.class);

        given(ucpServiceMock.selectAllElementsOrderedForCategoryViewName("xoxo")).willReturn(Lists.newArrayList(firstElement, secondElement));

        // when
        String viewName = ucpController.category("xoxo");

        // then
        assertThat(viewName).isEqualTo("redirect:/ucp/xoxo/bar");
    }
}