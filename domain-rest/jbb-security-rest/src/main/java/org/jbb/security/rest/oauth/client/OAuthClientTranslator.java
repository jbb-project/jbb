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

import org.jbb.security.api.oauth.EditOAuthClient;
import org.jbb.security.api.oauth.OAuthClient;
import org.jbb.security.api.oauth.OAuthClientSearchCriteria;
import org.jbb.security.api.oauth.SecretOAuthClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class OAuthClientTranslator {

    public OAuthClientDto toDto(OAuthClient client) {
        return OAuthClientDto.builder()
                .clientId(client.getClientId())
                .displayedName(client.getDisplayedName())
                .grantTypes(client.getGrantTypes())
                .scopes(client.getScopes())
                .build();
    }

    public OAuthClient toModel(OAuthClientDto dto) {
        return OAuthClient.clientBuilder()
                .clientId(dto.getClientId())
                .displayedName(dto.getDisplayedName())
                .grantTypes(dto.getGrantTypes())
                .scopes(dto.getScopes())
                .build();
    }

    public SecretOAuthClientDto toSecretDto(SecretOAuthClient createdClient) {
        return SecretOAuthClientDto.builder()
                .clientId(createdClient.getClientId())
                .clientSecret(createdClient.getClientSecret())
                .displayedName(createdClient.getDisplayedName())
                .grantTypes(createdClient.getGrantTypes())
                .scopes(createdClient.getScopes())
                .build();
    }

    public EditOAuthClient toEditModel(EditOAuthClientDto dto) {
        return EditOAuthClient.builder()
                .displayedName(dto.getDisplayedName())
                .grantTypes(dto.getGrantTypes())
                .scopes(dto.getScopes())
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
}
