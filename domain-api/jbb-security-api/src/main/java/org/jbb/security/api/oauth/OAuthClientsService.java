/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.api.oauth;

import org.springframework.data.domain.Page;

import java.util.Optional;

public interface OAuthClientsService {

    Optional<OAuthClient> getClient(String clientId);

    OAuthClient getClientChecked(String clientId) throws OAuthClientNotFoundException;

    Page<OAuthClient> getClientsWithCriteria(OAuthClientSearchCriteria criteria);

    SecretOAuthClient createClient(OAuthClient newClient);

    OAuthClient updateClient(String clientId, EditOAuthClient updatedClient) throws OAuthClientNotFoundException;

    String generateClientSecret(String clientId) throws OAuthClientNotFoundException;

    void removeClient(String clientId) throws OAuthClientNotFoundException;
}
