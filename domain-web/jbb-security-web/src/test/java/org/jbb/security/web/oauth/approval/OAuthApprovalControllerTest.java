/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.oauth.approval;


import com.google.common.collect.Sets;

import org.jbb.security.api.oauth.OAuthClient;
import org.jbb.security.api.oauth.OAuthClientsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class OAuthApprovalControllerTest {

    @Mock
    private OAuthClientsService oAuthClientsServiceMock;

    @Mock
    private AuthorizationRequest authorizationRequestMock;

    @InjectMocks
    private OAuthApprovalController oAuthApprovalController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = standaloneSetup(oAuthApprovalController).build();
    }

    @Test
    public void redirectToMainPageWhenNoAuthorizationRequest() throws Exception {
        mockMvc.perform(get("/oauth/confirm_access"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }

    @Test
    public void setScopesAndDisplayedNameOfClientInModel() throws Exception {
        OAuthClient client = OAuthClient.clientBuilder()
                .clientId("omc")
                .displayedName("OMC Client")
                .build();
        BDDMockito.given(oAuthClientsServiceMock.getClientChecked("omc")).willReturn(client);
        BDDMockito.given(authorizationRequestMock.getClientId()).willReturn("omc");
        BDDMockito.given(authorizationRequestMock.getScope()).willReturn(Sets.newHashSet("board:read"));
        mockMvc.perform(get("/oauth/confirm_access").flashAttr("authorizationRequest", authorizationRequestMock))
                .andExpect(MockMvcResultMatchers.view().name("oauthApprove"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("oAuthClient", "requestedScopes"));
    }
}