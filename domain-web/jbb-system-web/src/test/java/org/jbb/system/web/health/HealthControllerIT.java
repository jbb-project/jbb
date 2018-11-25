/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.health;

import org.jbb.system.api.health.HealthCheckService;
import org.jbb.system.api.health.HealthResult;
import org.jbb.system.api.health.HealthStatus;
import org.jbb.system.web.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class HealthControllerIT extends BaseIT {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    HealthCheckService healthCheckServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void shouldSetHealthView_whenCallHealthGET() throws Exception {
        // given
        LocalDateTime lastCheckTime = LocalDateTime.now();
        given(healthCheckServiceMock.getHealth()).willReturn(HealthResult.builder()
                .status(HealthStatus.HEALTHY)
                .lastCheckedAt(lastCheckTime)
                .build());

        // when
        ResultActions result = mockMvc.perform(get("/health"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("health"))
                .andExpect(model().attribute("healthStatus", "HEALTHY"))
                .andExpect(model().attributeExists("healthLastCheck"));
    }

}