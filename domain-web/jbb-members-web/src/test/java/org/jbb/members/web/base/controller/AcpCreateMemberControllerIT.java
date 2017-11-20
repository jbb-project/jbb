/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.jbb.members.web.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class AcpCreateMemberControllerIT extends BaseIT {
    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void shouldSetView_whenGET() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/acp/members/create"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/members/create"));
    }

    @Test
    public void shouldSetView_whenPOST() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/acp/members/create")
                .param("username", "john")
                .param("displayedName", "John")
                .param("email", "john@john.pl"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/members/create"));
    }

}