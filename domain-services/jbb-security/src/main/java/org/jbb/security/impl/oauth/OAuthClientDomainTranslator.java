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

import org.jbb.security.api.oauth.OAuthClient;
import org.jbb.security.impl.oauth.model.OAuthClientEntity;
import org.springframework.stereotype.Component;

@Component
public class OAuthClientDomainTranslator {

    public OAuthClient toModel(OAuthClientEntity entity) {
        return OAuthClient.clientBuilder()
                .clientId(entity.getClientId())
                .displayedName(entity.getDisplayedName())
                .grantTypes(entity.getGrantTypes())
                .scopes(entity.getScopes())
                .build();
    }
}
