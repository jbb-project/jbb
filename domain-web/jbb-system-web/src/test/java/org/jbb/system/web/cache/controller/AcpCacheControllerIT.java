/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.cache.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.lib.test.MockSpringSecurityConfig;
import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.api.cache.CacheSettingsService;
import org.jbb.system.web.SystemConfigMock;
import org.jbb.system.web.SystemWebConfig;
import org.jbb.system.web.cache.form.CacheSettingsForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CommonsConfig.class, MvcConfig.class, SystemWebConfig.class, PropertiesConfig.class,
        SystemConfigMock.class, MockCommonsConfig.class, MockSpringSecurityConfig.class})
public class AcpCacheControllerIT {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private CacheSettingsService cacheSettingsServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
        Mockito.reset(cacheSettingsServiceMock);
    }

    @Test
    public void shouldPutCurrentCacheSettingsToModel_whenGET() throws Exception {
        // given
        CacheSettings cacheSettings = mock(CacheSettings.class);
        given(cacheSettings.isApplicationCacheEnabled()).willReturn(true);
        given(cacheSettings.isSecondLevelCacheEnabled()).willReturn(true);
        given(cacheSettings.isQueryCacheEnabled()).willReturn(false);
        given(cacheSettings.getCurrentCacheProvider()).willReturn(CacheProvider.CAFFEINE_EMBEDDED);

        given(cacheSettingsServiceMock.getCacheSettings()).willReturn(cacheSettings);

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/cache"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/cache"));

        CacheSettingsForm cacheSettingsForm = (CacheSettingsForm) result.andReturn()
                .getModelAndView().getModel().get("cacheSettingsForm");

        assertThat(cacheSettingsForm.isApplicationCacheEnabled()).isTrue();
        assertThat(cacheSettingsForm.isSecondLevelCacheEnabled()).isTrue();
        assertThat(cacheSettingsForm.isQueryCacheEnabled()).isFalse();
        assertThat(cacheSettingsForm.getProviderName()).isEqualTo("CAFFEINE_EMBEDDED");
    }

    @Test
    public void shouldSetFlag_whenPOST_ok() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/acp/general/cache")
            .param("providerName", "CAFFEINE_EMBEDDED"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/general/cache"))
                .andExpect(flash().attribute("cacheSettingsFormSaved", true));

        verify(cacheSettingsServiceMock, times(1)).setCacheSettings(any(CacheSettings.class));
    }
}