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

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.jbb.lib.commons.security.OAuthScope;
import org.jbb.security.api.oauth.GrantType;
import org.jbb.security.api.oauth.OAuthClient;
import org.jbb.security.api.oauth.OAuthClientException;
import org.jbb.security.api.oauth.OAuthClientsService;
import org.jbb.security.api.oauth.SecretOAuthClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class AcpOAuthClientControllerTest {

    @Mock
    private OAuthClientsService oAuthClientsServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = standaloneSetup(new AcpOAuthClientController(oAuthClientsServiceMock,
                new OAuthClientFormTranslator(), new OAuthClientErrorsBindingMapper()))
                .build();
    }

    @Test
    public void shouldPutClientForm_whenNewClientView() throws Exception {
        mockMvc.perform(get("/acp/system/oauth/clients"))
                .andExpect(MockMvcResultMatchers.view().name("acp/system/oauth-client"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("clientForm"));
    }

    @Test
    public void shouldPutClientForm_whenGettingClient() throws Exception {
        // given
        given(oAuthClientsServiceMock.getClientChecked("omc")).willReturn(exampleOAuthClient());

        // when then
        mockMvc.perform(get("/acp/system/oauth/clients").param("id", "omc"))
                .andExpect(MockMvcResultMatchers.view().name("acp/system/oauth-client"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("clientForm"));
    }

    @Test
    public void shouldPutClientForm_whenFailedFormHappened() throws Exception {
        mockMvc.perform(get("/acp/system/oauth/clients").flashAttr("clientFormSaved", true)
                .flashAttr("clientForm", exampleClientForm(true)))
                .andExpect(MockMvcResultMatchers.view().name("acp/system/oauth-client"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("clientForm"));
    }

    @Test
    public void shouldRedirectWhenDeleteClient() throws Exception {
        mockMvc.perform(post("/acp/system/oauth/clients/delete").param("id", "test"))
                // then
                .andExpect(MockMvcResultMatchers.redirectedUrl("/acp/system/oauth"));
    }

    @Test
    public void shouldRedirectWhenRegenerateSecret() throws Exception {
        mockMvc.perform(post("/acp/system/oauth/clients/regenerate").param("id", "test"))
                // then
                .andExpect(MockMvcResultMatchers.redirectedUrl("/acp/system/oauth/clients?id=test"));
    }

    @Test
    public void shouldRedirectWhenCreatingNewClient() throws Exception {
        // given
        given(oAuthClientsServiceMock.createClient(any())).willReturn(exampleSecretOAuthClient());
        // when
        mockMvc.perform(post("/acp/system/oauth/clients").flashAttr("clientForm", exampleClientForm(true))
        )
                // then
                .andExpect(MockMvcResultMatchers.redirectedUrl("/acp/system/oauth/clients?id=omc"));
    }

    @Test
    public void shouldRedirectWhenUpdatingClient() throws Exception {
        // given
        given(oAuthClientsServiceMock.updateClient(any(), any())).willReturn(exampleOAuthClient());
        // when
        mockMvc.perform(post("/acp/system/oauth/clients").flashAttr("clientForm", exampleClientForm(false))
        )
                // then
                .andExpect(MockMvcResultMatchers.redirectedUrl("/acp/system/oauth/clients?id=omc"));
    }

    @Test
    public void shouldRedirectWhenUpdatingClientFailed() throws Exception {
        // given
        given(oAuthClientsServiceMock.updateClient(any(), any())).willThrow(new OAuthClientException(Sets.newHashSet()));
        // when
        mockMvc.perform(post("/acp/system/oauth/clients").flashAttr("clientForm", exampleClientForm(false))
        )
                // then
                .andExpect(MockMvcResultMatchers.redirectedUrl("/acp/system/oauth/clients?id=aaa"));
    }


    private OAuthClientForm exampleClientForm(boolean addingMode) {
        Map<String, Boolean> grantTypes = Maps.newHashMap();
        grantTypes.put("password", true);
        Map<String, Boolean> scopes = Maps.newHashMap();
        scopes.put("board:read", true);
        final OAuthClientForm form = new OAuthClientForm();
        form.setClientId("aaa");
        form.setDisplayedName("AAA");
        form.setDescription("aaaa");
        form.setAddingMode(addingMode);
        form.setRedirectUris("localhost\naaa.com");
        form.setGrantTypes(grantTypes);
        form.setScopes(scopes);
        return form;
    }

    private OAuthClient exampleOAuthClient() {
        return OAuthClient.clientBuilder()
                .clientId("omc")
                .displayedName("OMC")
                .description(Optional.empty())
                .scopes(Sets.newHashSet(OAuthScope.MEMBER_READ))
                .grantTypes(Sets.newHashSet(GrantType.CLIENT_CREDENTIALS))
                .redirectUris(Sets.newHashSet("localhost:9000"))
                .build();
    }

    private SecretOAuthClient exampleSecretOAuthClient() {
        return SecretOAuthClient.secretClientBuilder()
                .clientSecret("secret")
                .clientId("omc")
                .displayedName("OMC")
                .description(Optional.empty())
                .scopes(Sets.newHashSet(OAuthScope.MEMBER_READ))
                .grantTypes(Sets.newHashSet(GrantType.CLIENT_CREDENTIALS))
                .redirectUris(Sets.newHashSet("localhost:9000"))
                .build();
    }

}