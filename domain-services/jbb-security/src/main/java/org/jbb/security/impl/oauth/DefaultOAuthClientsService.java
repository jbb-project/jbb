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
import org.jbb.security.api.oauth.OAuthClientException;
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
import org.jbb.security.impl.oauth.model.validation.create.CreateGroup;
import org.jbb.security.impl.oauth.model.validation.update.UpdateGroup;
import org.jbb.security.impl.oauth.search.OAuthClientSpecificationCreator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultOAuthClientsService implements OAuthClientsService {

    private final OAuthClientRepository clientRepository;
    private final OAuthClientDomainTranslator domainTranslator;
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;
    private final OAuthClientSpecificationCreator specificationCreator;
    private final JbbEventBus eventBus;
    private final ClientSecretGenerator secretGenerator;

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
        PageRequest pageRequest = PageRequest.of(criteria.getPageRequest().getPageNumber(),
                criteria.getPageRequest().getPageSize(), new Sort(Sort.Direction.DESC, "createDateTime"));
        return clientRepository.findAll(specificationCreator.createSpecification(criteria), pageRequest)
                .map(domainTranslator::toModel);
    }

    @Override
    @Transactional
    public SecretOAuthClient createClient(OAuthClient newClient) {
        Validate.notNull(newClient);
        OAuthClientEntity clientEntity = domainTranslator.toEntity(newClient);
        String clientSecret = secretGenerator.generateSecret();
        clientEntity.setClientSecret(passwordEncoder.encode(clientSecret));
        Set<ConstraintViolation<OAuthClientEntity>> violations = validator.validate(clientEntity, Default.class, CreateGroup.class);
        if (!violations.isEmpty()) {
            throw new OAuthClientException(violations);
        }
        OAuthClientEntity savedClientEntity = clientRepository.save(clientEntity);
        eventBus.post(new OAuthClientAddedEvent(newClient.getClientId()));
        return domainTranslator.toSecretModel(savedClientEntity, clientSecret);
    }

    @Override
    @Transactional
    public OAuthClient updateClient(String clientId, EditOAuthClient updatedClient) throws OAuthClientNotFoundException {
        Validate.notBlank(clientId);
        Validate.notNull(updatedClient);
        OAuthClientEntity client = clientRepository.findByClientId(clientId).orElseThrow(() -> new OAuthClientNotFoundException(clientId));
        client.setDisplayedName(updatedClient.getDisplayedName());
        client.setGrantTypes(updatedClient.getGrantTypes());
        client.setScopes(updatedClient.getScopes());
        Set<ConstraintViolation<OAuthClientEntity>> violations = validator.validate(client, Default.class, UpdateGroup.class);
        if (!violations.isEmpty()) {
            throw new OAuthClientException(violations);
        }
        OAuthClientEntity updatedClientEntity = clientRepository.save(client);
        eventBus.post(new OAuthClientChangedEvent(clientId));
        return domainTranslator.toModel(updatedClientEntity);
    }

    @Override
    @Transactional
    public String generateClientSecret(String clientId) throws OAuthClientNotFoundException {
        Validate.notBlank(clientId);
        OAuthClientEntity client = clientRepository.findByClientId(clientId).orElseThrow(() -> new OAuthClientNotFoundException(clientId));
        String newSecret = secretGenerator.generateSecret();
        client.setClientSecret(newSecret);
        clientRepository.save(client);
        eventBus.post(new OAuthClientSecretRegeneratedEvent(clientId));
        return newSecret;
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
