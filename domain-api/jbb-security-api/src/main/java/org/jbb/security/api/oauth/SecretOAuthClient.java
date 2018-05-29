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
    public SecretOAuthClient(String displayedName, Set<GrantType> grantTypes, Set<String> scopes,
                             String clientId, String clientSecret) {
        super(displayedName, grantTypes, scopes, clientId);
        this.clientSecret = clientSecret;
    }
}
