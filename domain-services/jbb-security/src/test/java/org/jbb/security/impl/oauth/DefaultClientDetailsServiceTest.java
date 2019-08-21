/*
 * Copyright (C) 2019 the original author or authors.
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
import org.jbb.security.impl.oauth.dao.OAuthClientRepository;
import org.jbb.security.impl.oauth.model.OAuthClientEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DefaultClientDetailsServiceTest {

    @Mock
    private OAuthClientRepository clientRepositoryMock;

    @InjectMocks
    private DefaultClientDetailsService defaultClientDetailsService;

    @Test
    public void throwExceptionWhenClientNotFound() {
        // given
        given(clientRepositoryMock.findByClientId(any())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> defaultClientDetailsService.loadClientByClientId("anyClientId"))
                .isInstanceOf(ClientRegistrationException.class);
    }

    @Test
    public void returnsValidClientDetails() {
        // given
        given(clientRepositoryMock.findByClientId(any())).willReturn(Optional.of(
                OAuthClientEntity.builder()
                        .clientId("testClient")
                        .clientSecret("aaa")
                        .displayedName("disp")
                        .description("desc")
                        .grantTypes(Sets.newHashSet(GrantType.PASSWORD))
                        .scopes(Sets.newHashSet(OAuthScope.MEMBER_READ))
                        .redirectUris(Sets.newHashSet("http://omc.com"))
                        .build()
        ));

        // when
        ClientDetails clientDetails = defaultClientDetailsService.loadClientByClientId("testClient");

        // then
        assertThat(clientDetails.getClientId()).isEqualTo("testClient");
        assertThat(clientDetails.getClientSecret()).isEqualTo("aaa");
        assertThat(clientDetails.getScope()).isEqualTo(Sets.newHashSet("member:read"));
        assertThat(clientDetails.getAuthorizedGrantTypes()).isEqualTo(Sets.newHashSet("password"));
        assertThat(clientDetails.getRegisteredRedirectUri()).isEqualTo(Sets.newHashSet("http://omc.com", "/oauth-redirect"));
    }
}