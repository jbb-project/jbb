/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.oauth;

import com.google.common.collect.Sets;

import org.jbb.lib.commons.security.OAuthScope;
import org.jbb.security.api.oauth.GrantType;
import org.jbb.security.api.oauth.OAuthClient;
import org.jbb.security.api.oauth.OAuthClientNotFoundException;
import org.jbb.security.impl.BaseIT;
import org.jbb.security.impl.oauth.dao.OAuthClientRepository;
import org.jbb.security.impl.oauth.model.OAuthClientEntity;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuthClientsServiceIT extends BaseIT {

    @Autowired
    private OAuthClientRepository clientRepository;

    @Autowired
    private DefaultOAuthClientsService oAuthClientsService;

    @Before
    public void cleanUp() {
        clientRepository.deleteAll();
    }

    @Test
    public void shouldReturnOptionalEmpty_whenGetClientIdNull() {
        // when
        Optional<OAuthClient> client = oAuthClientsService.getClient(null);

        // then
        assertThat(client).isEmpty();
    }

    @Test
    public void shouldReturnOptionalEmpty_whenGetClientIdBlank() {
        // when
        Optional<OAuthClient> client = oAuthClientsService.getClient("");

        // then
        assertThat(client).isEmpty();
    }

    @Test
    public void shouldReturnOptionalEmpty_whenGetClientNotFound() {
        // when
        Optional<OAuthClient> client = oAuthClientsService.getClient("notfound");

        // then
        assertThat(client).isEmpty();
    }

    @Test
    public void shouldReturnClient_whenFound() {
        // given
        clientRepository.save(exampleValidClient());

        // when
        Optional<OAuthClient> clientOpt = oAuthClientsService.getClient("example-client");

        // then
        OAuthClient client = clientOpt.get();
        assertThat(client.getClientId()).isEqualTo("example-client");
        assertThat(client.getDisplayedName()).isEqualTo("Example Client");
        assertThat(client.getDescription().get()).isEqualTo("Example OAuth client for testing");
        assertThat(client.getGrantTypes()).isEqualTo(Sets.newHashSet(GrantType.CLIENT_CREDENTIALS));
        assertThat(client.getScopes()).isEqualTo(Sets.newHashSet(OAuthScope.MEMBER_READ_WRITE));
        assertThat(client.getRedirectUris()).isEqualTo(Sets.newHashSet("http://localhost:8090"));
    }

    @Test
    public void shouldReturnClientChecked_whenFound() throws OAuthClientNotFoundException {
        // given
        clientRepository.save(exampleValidClient());

        // when
        OAuthClient client = oAuthClientsService.getClientChecked("example-client");

        // then
        assertThat(client.getClientId()).isEqualTo("example-client");
    }

    @Test(expected = OAuthClientNotFoundException.class)
    public void shouldThrowOAuthClientNotFoundException_whenGetClientCheckedNotFound() throws OAuthClientNotFoundException {
        // when
        oAuthClientsService.getClientChecked("not-existing-client");

        // then
        // throw OAuthClientNotFoundException
    }

    private OAuthClientEntity exampleValidClient() {
        return OAuthClientEntity.builder()
                .clientId("example-client")
                .displayedName("Example Client")
                .description("Example OAuth client for testing")
                .clientSecret("aaaa")
                .grantTypes(Sets.newHashSet(GrantType.CLIENT_CREDENTIALS))
                .scopes(Sets.newHashSet(OAuthScope.MEMBER_READ_WRITE))
                .redirectUris(Sets.newHashSet("http://localhost:8090"))
                .build();
    }
}