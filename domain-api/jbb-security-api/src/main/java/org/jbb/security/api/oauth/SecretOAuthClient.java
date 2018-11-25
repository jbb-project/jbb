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

import org.jbb.lib.commons.security.OAuthScope;

import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecretOAuthClient extends OAuthClient {

    @NotBlank
    private String clientSecret;

    @Builder(builderMethodName = "secretClientBuilder")
    public SecretOAuthClient(String displayedName, Optional<String> description, Set<GrantType> grantTypes,
                             Set<OAuthScope> scopes, Set<String> redirectUris, String clientId, String clientSecret) {
        super(displayedName, description, grantTypes, scopes, redirectUris, clientId);
        this.clientSecret = clientSecret;
    }
}
