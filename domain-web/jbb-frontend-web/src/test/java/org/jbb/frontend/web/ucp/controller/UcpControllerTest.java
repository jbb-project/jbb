/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.ucp.controller;

import org.jbb.frontend.api.ucp.UcpService;
import org.jbb.frontend.api.ucp.UcpStructure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UcpControllerTest {
    @Mock
    private UcpService ucpServiceMock;

    @InjectMocks
    private UcpController ucpController;

    @Test
    public void shouldRedirectToSubpageWithFirstCategoryName_whenUcpPathInvoked() {
        // given
        UcpStructure ucpStructure = new UcpStructure.Builder()
                .add(new UcpStructure.Category.Builder().viewName("foo").build()).build();
        given(ucpServiceMock.getUcpStructure()).willReturn(ucpStructure);

        // when
        String viewName = ucpController.ucpMain();

        // then
        assertThat(viewName).isEqualTo("redirect:/ucp/foo");
    }

    @Test
    public void shouldRedirectToSubsubpageWithFirstElementName_whenUcpSubpageCategoryPathInvoked() {
        // given
        UcpStructure ucpStructure = new UcpStructure.Builder()
                .add(new UcpStructure.Category.Builder().viewName("xoxo")
                        .add(UcpStructure.Element.of("Bar view", "bar"))
                        .build()).build();
        given(ucpServiceMock.getUcpStructure()).willReturn(ucpStructure);

        // when
        String viewName = ucpController.category("xoxo");

        // then
        assertThat(viewName).isEqualTo("redirect:/ucp/xoxo/bar");
    }
}