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
import org.jbb.security.api.oauth.OAuthClientNotFoundException;
import org.jbb.security.api.oauth.OAuthClientSearchCriteria;
import org.jbb.security.api.oauth.OAuthClientsService;
import org.jbb.security.api.oauth.SecretOAuthClient;
import org.jbb.security.event.OAuthClientAddedEvent;
import org.jbb.security.event.OAuthClientChangedEvent;
import org.jbb.security.event.OAuthClientRemovedEvent;
import org.jbb.security.event.OAuthClientSecretRegeneratedEvent;
import org.jbb.security.impl.oauth.dao.OAuthClientRepository;
import org.jbb.security.impl.oauth.model.OAuthClientEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultOAuthClientsService implements OAuthClientsService {

    private final OAuthClientRepository clientRepository;
    private final OAuthClientDomainTranslator domainTranslator;
    private final JbbEventBus eventBus;

    @Override
    @Transactional(readOnly = true)
    public Optional<OAuthClient> getClient(String clientId) {
        if (StringUtils.isBlank(clientId)) {
            return Optional.empty();
        }
        return clientRepository.findByClientId(clientId)
                .map(domainTranslator::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public OAuthClient getClientChecked(String clientId) throws OAuthClientNotFoundException {
        return getClient(clientId).orElseThrow(() -> new OAuthClientNotFoundException(clientId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OAuthClient> getClientsWithCriteria(OAuthClientSearchCriteria criteria) {
        Validate.notNull(criteria);
        return null;
    }

    @Override
    @Transactional
    public SecretOAuthClient createClient(OAuthClient newClient) {
        Validate.notNull(newClient);
        eventBus.post(new OAuthClientAddedEvent(newClient.getClientId()));
        return null;
    }

    @Override
    @Transactional
    public OAuthClient updateClient(String clientId, EditOAuthClient updatedClient) throws OAuthClientNotFoundException {
        Validate.notBlank(clientId);
        Validate.notNull(updatedClient);
        OAuthClientEntity client = clientRepository.findByClientId(clientId).orElseThrow(() -> new OAuthClientNotFoundException(clientId));
        eventBus.post(new OAuthClientChangedEvent(clientId));
        return null;
    }

    @Override
    @Transactional
    public String generateClientSecret(String clientId) throws OAuthClientNotFoundException {
        Validate.notBlank(clientId);
        OAuthClientEntity client = clientRepository.findByClientId(clientId).orElseThrow(() -> new OAuthClientNotFoundException(clientId));
        eventBus.post(new OAuthClientSecretRegeneratedEvent(clientId));
        return null;
    }

    @Override
    @Transactional
    public void removeClient(String clientId) throws OAuthClientNotFoundException {
        Validate.notBlank(clientId);
        OAuthClientEntity client = clientRepository.findByClientId(clientId).orElseThrow(() -> new OAuthClientNotFoundException(clientId));
        clientRepository.delete(client);
        eventBus.post(new OAuthClientRemovedEvent(clientId));
    }
}
