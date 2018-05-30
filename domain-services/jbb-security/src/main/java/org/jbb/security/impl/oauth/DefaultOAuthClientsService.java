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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.api.oauth.EditOAuthClient;
import org.jbb.security.api.oauth.OAuthClient;
import org.jbb.security.api.oauth.OAuthClientSearchCriteria;
import org.jbb.security.api.oauth.OAuthClientsService;
import org.jbb.security.api.oauth.SecretOAuthClient;
import org.jbb.security.impl.oauth.dao.OAuthClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultOAuthClientsService implements OAuthClientsService {

    private final OAuthClientRepository clientRepository;
    private final OAuthClientDomainTranslator domainTranslator;
    private final JbbEventBus eventBus;

    @Override
    public Optional<OAuthClient> getClient(String clientId) {
        if (StringUtils.isBlank(clientId)) {
            return Optional.empty();
        }
        return clientRepository.findByClientId(clientId)
                .map(domainTranslator::toModel);
    }

    @Override
    public Page<OAuthClient> getClientsWithCriteria(OAuthClientSearchCriteria criteria) {
        Validate.notNull(criteria);
        return null;
    }

    @Override
    public SecretOAuthClient createClient(OAuthClient newClient) {
        Validate.notNull(newClient);
        return null;
    }

    @Override
    public OAuthClient updateClient(String clientId, EditOAuthClient updatedClient) {
        Validate.notBlank(clientId);
        Validate.notNull(updatedClient);
        return null;
    }

    @Override
    public String generateClientSecret(String clientId) {
        Validate.notBlank(clientId);
        return null;
    }

    @Override
    public void removeClient(String clientId) {
        Validate.notBlank(clientId);

    }
}
