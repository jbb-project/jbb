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
import org.jbb.security.impl.oauth.dao.OAuthClientRepository;
import org.jbb.security.impl.oauth.model.OAuthClientEntity;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Component("defaultClientDetailsService")
@RequiredArgsConstructor
public class DefaultClientDetailsService implements ClientDetailsService {
    private final OAuthClientRepository clientRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        OAuthClientEntity client = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new ClientRegistrationException("Client '" + clientId + "' not found"));
        return buildBaseClientDetails(client);
    }

    private ClientDetails buildBaseClientDetails(OAuthClientEntity client) {
        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId(client.getClientId());
        clientDetails.setClientSecret(client.getClientSecret());
        clientDetails.setScope(buildScopesString(client.getScopes()));
        clientDetails.setAuthorizedGrantTypes(buildGrantTypesString(client.getGrantTypes()));
        clientDetails.setRegisteredRedirectUri(Sets.newHashSet("/oauth-redirect"));
        return clientDetails;
    }

    private List<String> buildScopesString(Set<OAuthScope> scopes) {
        return scopes.stream().map(Enum::name).collect(Collectors.toList());
    }

    private List<String> buildGrantTypesString(Set<GrantType> grantTypes) {
        return grantTypes.stream().map(GrantType::getName).collect(Collectors.toList());
    }
}
