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

import org.assertj.core.util.Lists;
import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.api.cache.CacheSettingsService;
import org.jbb.system.api.cache.HazelcastClientSettings;
import org.jbb.system.api.cache.HazelcastServerSettings;
import org.jbb.system.web.BaseIT;
import org.jbb.system.web.cache.form.CacheSettingsForm;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AcpCacheControllerIT extends BaseIT {
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
        givenCurrentCacheSettings();

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
        assertThat(cacheSettingsForm.getProviderName()).isEqualTo("CAFFEINE");
    }

    @Test
    public void shouldSetFlag_whenPOST_ok() throws Exception {
        // given
        givenCurrentCacheSettings();

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/cache")
                .param("providerName", "CAFFEINE")
                .param("hazelcastServerSettings.members", "127.0.0.2:4444, 127.0.0.2:5555")
                .param("hazelcastClientSettings.members", "127.0.0.2:4444, 127.0.0.2:5555")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/general/cache"))
                .andExpect(flash().attribute("cacheSettingsFormSaved", true));

        verify(cacheSettingsServiceMock, times(1)).setCacheSettings(any(CacheSettings.class));
    }

    private void givenCurrentCacheSettings() {
        CacheSettings cacheSettings = CacheSettings.builder()
                .applicationCacheEnabled(true)
                .secondLevelCacheEnabled(true)
                .queryCacheEnabled(false)
                .currentCacheProvider(CacheProvider.CAFFEINE)
                .build();

        HazelcastServerSettings serverSettings = new HazelcastServerSettings();
        serverSettings.setServerPort(1234);
        serverSettings.setGroupName("jbb");
        serverSettings.setGroupPassword("jbb");
        serverSettings.setMembers(Lists.newArrayList("127.0.0.1:3333"));
        cacheSettings.setHazelcastServerSettings(serverSettings);

        HazelcastClientSettings clientSettings = new HazelcastClientSettings();
        clientSettings.setGroupName("jbb");
        clientSettings.setGroupPassword("jbb");
        clientSettings.setMembers(Lists.newArrayList("127.0.0.1:5676"));
        clientSettings.setConnectionAttemptLimit(3);
        clientSettings.setConnectionTimeout(Duration.ofMinutes(1));
        clientSettings.setConnectionAttemptPeriod(Duration.ofMinutes(5));
        cacheSettings.setHazelcastClientSettings(clientSettings);

        given(cacheSettingsServiceMock.getCacheSettings()).willReturn(cacheSettings);
    }
}