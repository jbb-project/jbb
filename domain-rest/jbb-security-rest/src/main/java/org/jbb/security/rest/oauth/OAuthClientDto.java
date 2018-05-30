/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.oauth;

import org.jbb.lib.commons.security.OAuthScope;
import org.jbb.security.api.oauth.GrantType;

import java.util.Set;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@ApiModel("OAuthClient")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuthClientDto {

    private String clientId;
    private String displayedName;
    private Set<GrantType> grantTypes;
    private Set<OAuthScope> scopes;

}
