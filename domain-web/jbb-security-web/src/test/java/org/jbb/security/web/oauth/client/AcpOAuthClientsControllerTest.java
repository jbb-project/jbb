/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.oauth.client;

import com.google.common.collect.Sets;

import org.assertj.core.util.Lists;
import org.jbb.lib.commons.security.OAuthScope;
import org.jbb.security.api.oauth.GrantType;
import org.jbb.security.api.oauth.OAuthClient;
import org.jbb.security.api.oauth.OAuthClientsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class AcpOAuthClientsControllerTest {

    @Mock
    private OAuthClientsService oAuthClientsServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = standaloneSetup(new AcpOAuthClientsController(oAuthClientsServiceMock,
                new SearchClientCriteriaFactory(), new OAuthClientRowTranslator()))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    public void shouldPutSearchForm_whenGetAcpPage() throws Exception {
        mockMvc.perform(get("/acp/system/oauth"))
                .andExpect(MockMvcResultMatchers.view().name("acp/system/oauth"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("oauthClientSearchForm"));
    }

    @Test
    public void shouldPutResultPage_whenPostSearchForm() throws Exception {
        // given
        BDDMockito.given(oAuthClientsServiceMock.getClientsWithCriteria(any()))
                .willReturn(new PageImpl<>(Lists.newArrayList(exampleOAuthClient())));

        // when
        mockMvc.perform(post("/acp/system/oauth").param("clientId", "test"))
                // then
                .andExpect(MockMvcResultMatchers.redirectedUrl("/acp/system/oauth"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("resultPage"));
    }

    private OAuthClient exampleOAuthClient() {
        return OAuthClient.clientBuilder()
                .clientId("omc")
                .displayedName("OMC")
                .description(Optional.empty())
                .scopes(Sets.newHashSet(OAuthScope.MEMBER_READ))
                .grantTypes(Sets.newHashSet(GrantType.CLIENT_CREDENTIALS))
                .build();
    }
}