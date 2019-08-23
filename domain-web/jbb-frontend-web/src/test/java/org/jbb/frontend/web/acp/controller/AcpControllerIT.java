/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.acp.controller;


import org.jbb.frontend.api.acp.AcpService;
import org.jbb.frontend.api.acp.AcpStructure;
import org.jbb.frontend.web.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AcpControllerIT extends BaseIT {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    AcpService acpServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void shouldRedirectToFirstAcpSubpage_whenRootAcpUrlInvoked() throws Exception {
        // given
        AcpStructure.Category category = mock(AcpStructure.Category.class);
        given(category.getViewName()).willReturn("general");
        AcpStructure acpStructure = new AcpStructure.Builder().add(category).build();
        given(acpServiceMock.getAcpStructure()).willReturn(acpStructure);

        // when
        ResultActions result = mockMvc.perform(get("/acp"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/general"));
    }

}