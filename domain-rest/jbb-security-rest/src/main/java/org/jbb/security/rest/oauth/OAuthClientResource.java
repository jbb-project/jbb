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

import com.google.common.collect.Lists;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.paging.PageDto;
import org.jbb.security.api.oauth.OAuthClientsService;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestAuthorize.IS_AN_ADMINISTRATOR;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.security.rest.SecurityRestConstants.CLIENT_ID;
import static org.jbb.security.rest.SecurityRestConstants.CLIENT_ID_VAR;
import static org.jbb.security.rest.SecurityRestConstants.CLIENT_SECRET;
import static org.jbb.security.rest.SecurityRestConstants.OAUTH_CLIENTS;

@RestController
@RequiredArgsConstructor
@PreAuthorize(IS_AN_ADMINISTRATOR)
@Api(tags = API_V1 + OAUTH_CLIENTS)
@RequestMapping(value = API_V1 + OAUTH_CLIENTS, produces = MediaType.APPLICATION_JSON_VALUE)
public class OAuthClientResource {

    private final OAuthClientsService oAuthClientsService;

    @GetMapping
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Gets OAuth clients")
    public PageDto<OAuthClientDto> clientsGet(
            @Validated @ModelAttribute OAuthClientCriteriaDto clientCriteria) {
        return PageDto.getDto(new PageImpl<>(Lists.newArrayList()));
    }

    @GetMapping(CLIENT_ID)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Gets OAuth client by id")
    public OAuthClientDto clientGet(@PathVariable(CLIENT_ID_VAR) String clientId) {
        return OAuthClientDto.builder().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Creates new OAuth client")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    public SecretOAuthClientDto clientPost(@RequestBody OAuthClientDto clientDto) {
        return SecretOAuthClientDto.builder().build();
    }

    @PutMapping(value = CLIENT_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Updates OAuth client")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    public OAuthClientDto clientPut(@PathVariable(CLIENT_ID_VAR) String clientId,
                                    @RequestBody EditOAuthClientDto updatedClientDto) {
        return OAuthClientDto.builder().build();
    }

    @DeleteMapping(CLIENT_ID)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Removes OAuth client by id")
    public void clientDelete(@PathVariable(CLIENT_ID_VAR) String clientId) {

    }

    @PutMapping(value = CLIENT_ID + CLIENT_SECRET, params = "action=regenarate")
    @ApiOperation("Generates new client secret for OAuth client")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    public ClientSecretDto clientSecretPut(@PathVariable(CLIENT_ID_VAR) String clientId) {
        return ClientSecretDto.builder().build();
    }


}
