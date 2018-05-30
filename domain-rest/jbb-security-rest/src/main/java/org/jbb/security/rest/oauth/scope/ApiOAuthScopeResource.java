/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.oauth.scope;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.security.rest.SecurityRestConstants.API_OAUTH_SCOPES;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + API_OAUTH_SCOPES)
@RequestMapping(value = API_V1 + API_OAUTH_SCOPES, produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiOAuthScopeResource {

    private final ScopeProvider scopeProvider;

    private final ScopeTranslator scopeTranslator;

    @GetMapping
    @ErrorInfoCodes({})
    @ApiOperation("Gets api OAuth scopes")
    public OAuthScopesDto getApiOAuthScopes() {
        return new OAuthScopesDto(scopeProvider.getAllScopes().stream()
                .map(scopeTranslator::toDto)
                .collect(Collectors.toList()));
    }


}
