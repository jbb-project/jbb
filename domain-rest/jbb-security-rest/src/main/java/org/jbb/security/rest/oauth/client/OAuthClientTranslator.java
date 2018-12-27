/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.oauth.client;

import com.google.common.collect.Sets;

import org.jbb.lib.commons.security.OAuthScope;
import org.jbb.security.api.oauth.EditOAuthClient;
import org.jbb.security.api.oauth.OAuthClient;
import org.jbb.security.api.oauth.OAuthClientSearchCriteria;
import org.jbb.security.api.oauth.SecretOAuthClient;
import org.jbb.security.rest.oauth.client.exception.OAuthScopeUnknown;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OAuthClientTranslator {

    public OAuthClientDto toDto(OAuthClient client) {
        return OAuthClientDto.builder()
                .clientId(client.getClientId())
                .displayedName(client.getDisplayedName())
                .description(client.getDescription().orElse(null))
                .grantTypes(client.getGrantTypes())
                .scopes(toDtoScopes(client.getScopes()))
                .redirectUris(client.getRedirectUris())
                .build();
    }

    public OAuthClient toModel(OAuthClientDto dto) {
        return OAuthClient.clientBuilder()
                .clientId(dto.getClientId())
                .displayedName(dto.getDisplayedName())
                .description(Optional.ofNullable(dto.getDescription()))
                .grantTypes(dto.getGrantTypes())
                .scopes(toModelScopes(dto.getScopes()))
                .redirectUris(Optional.ofNullable(dto.getRedirectUris()).orElse(Sets.newHashSet()))
                .build();
    }

    public SecretOAuthClientDto toSecretDto(SecretOAuthClient createdClient) {
        return SecretOAuthClientDto.builder()
                .clientId(createdClient.getClientId())
                .clientSecret(createdClient.getClientSecret())
                .displayedName(createdClient.getDisplayedName())
                .description(createdClient.getDescription().orElse(null))
                .grantTypes(createdClient.getGrantTypes())
                .scopes(toDtoScopes(createdClient.getScopes()))
                .redirectUris(createdClient.getRedirectUris())
                .build();
    }

    public EditOAuthClient toEditModel(EditOAuthClientDto dto) {
        return EditOAuthClient.builder()
                .displayedName(dto.getDisplayedName())
                .description(Optional.ofNullable(dto.getDescription()))
                .grantTypes(dto.getGrantTypes())
                .scopes(toModelScopes(dto.getScopes()))
                .redirectUris(Optional.ofNullable(dto.getRedirectUris()).orElse(Sets.newHashSet()))
                .build();
    }

    public ClientSecretDto toSecretDto(String newSecret) {
        return ClientSecretDto.builder()
                .clientSecret(newSecret)
                .build();
    }

    public OAuthClientSearchCriteria toModel(OAuthClientCriteriaDto dto) {
        return OAuthClientSearchCriteria.builder()
                .clientId(dto.getClientId())
                .displayedName(dto.getDisplayedName())
                .pageRequest(PageRequest.of(dto.getPage(), dto.getPageSize()))
                .build();
    }

    private Set<String> toDtoScopes(Set<OAuthScope> scopes) {
        return scopes.stream().map(OAuthScope::getScopeName).collect(Collectors.toSet());
    }

    private Set<OAuthScope> toModelScopes(Set<String> scopes) {
        return scopes.stream().map(scope -> OAuthScope.ofName(scope)
                .orElseThrow(() -> new OAuthScopeUnknown(scope)))
                .collect(Collectors.toSet());
    }
}
