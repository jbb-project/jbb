/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.oauth.client;

import org.jbb.security.api.oauth.OAuthClient;
import org.springframework.stereotype.Component;

@Component
public class OAuthClientRowTranslator {

    public OAuthClientRow toRow(OAuthClient oAuthClient) {
        return OAuthClientRow.builder()
                .clientId(oAuthClient.getClientId())
                .displayedName(oAuthClient.getDisplayedName())
                .description(oAuthClient.getDescription().orElse(null))
                .build();
    }

}
