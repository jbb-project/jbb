/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.oauth.client;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.DefaultRestExceptionMapper;
import org.jbb.lib.restful.error.ErrorDetail;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.lib.restful.paging.PageDto;
import org.jbb.security.api.oauth.OAuthClient;
import org.jbb.security.api.oauth.OAuthClientException;
import org.jbb.security.api.oauth.OAuthClientNotFoundException;
import org.jbb.security.api.oauth.OAuthClientsService;
import org.jbb.security.api.oauth.SecretOAuthClient;
import org.jbb.security.rest.oauth.client.exception.OAuthScopeUnknown;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import javax.validation.ConstraintViolation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_OAUTH_CLIENT;
import static org.jbb.lib.restful.domain.ErrorInfo.OAUTH_CLIENT_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.lib.restful.domain.ErrorInfo.UNKNOWN_OAUTH_SCOPE;
import static org.jbb.security.rest.SecurityRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_OAUTH_CLIENT_READ_SCOPE;
import static org.jbb.security.rest.SecurityRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_OAUTH_CLIENT_READ_WRITE_SCOPE;
import static org.jbb.security.rest.SecurityRestConstants.CLIENT_ID;
import static org.jbb.security.rest.SecurityRestConstants.CLIENT_ID_VAR;
import static org.jbb.security.rest.SecurityRestConstants.CLIENT_SECRET;
import static org.jbb.security.rest.SecurityRestConstants.OAUTH_CLIENTS;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + OAUTH_CLIENTS)
@RequestMapping(value = API_V1 + OAUTH_CLIENTS, produces = MediaType.APPLICATION_JSON_VALUE)
public class OAuthClientResource {

    private final OAuthClientsService oAuthClientsService;

    private final OAuthClientTranslator clientTranslator;
    private final DefaultRestExceptionMapper exceptionMapper;

    @GetMapping
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Gets OAuth clients")
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_OAUTH_CLIENT_READ_SCOPE)
    public PageDto<OAuthClientDto> clientsGet(
            @Validated @ModelAttribute OAuthClientCriteriaDto clientCriteria) {
        return PageDto.getDto(oAuthClientsService
                .getClientsWithCriteria(clientTranslator.toModel(clientCriteria))
                .map(clientTranslator::toDto));
    }

    @GetMapping(CLIENT_ID)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN, OAUTH_CLIENT_NOT_FOUND})
    @ApiOperation("Gets OAuth client by id")
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_OAUTH_CLIENT_READ_SCOPE)
    public OAuthClientDto clientGet(@PathVariable(CLIENT_ID_VAR) String clientId) throws OAuthClientNotFoundException {
        return clientTranslator.toDto(oAuthClientsService.getClientChecked(clientId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Creates new OAuth client")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_OAUTH_CLIENT_READ_WRITE_SCOPE)
    public SecretOAuthClientDto clientPost(@RequestBody OAuthClientDto clientDto) {
        SecretOAuthClient createdClient = oAuthClientsService.createClient(clientTranslator.toModel(clientDto));
        return clientTranslator.toSecretDto(createdClient);
    }

    @PutMapping(value = CLIENT_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Updates OAuth client")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN, OAUTH_CLIENT_NOT_FOUND})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_OAUTH_CLIENT_READ_WRITE_SCOPE)
    public OAuthClientDto clientPut(@PathVariable(CLIENT_ID_VAR) String clientId,
                                    @RequestBody EditOAuthClientDto updatedClientDto) throws OAuthClientNotFoundException {
        OAuthClient updatedClient = oAuthClientsService.updateClient(clientId, clientTranslator.toEditModel(updatedClientDto));
        return clientTranslator.toDto(updatedClient);
    }

    @DeleteMapping(CLIENT_ID)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN, OAUTH_CLIENT_NOT_FOUND})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Removes OAuth client by id")
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_OAUTH_CLIENT_READ_WRITE_SCOPE)
    public void clientDelete(@PathVariable(CLIENT_ID_VAR) String clientId) throws OAuthClientNotFoundException {
        oAuthClientsService.removeClient(clientId);
    }

    @PutMapping(value = CLIENT_ID + CLIENT_SECRET, params = "action=regenarate")
    @ApiOperation("Generates a new client secret for OAuth client")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN, OAUTH_CLIENT_NOT_FOUND})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_OAUTH_CLIENT_READ_WRITE_SCOPE)
    public ClientSecretDto clientSecretPut(@PathVariable(CLIENT_ID_VAR) String clientId) throws OAuthClientNotFoundException {
        return clientTranslator.toSecretDto(oAuthClientsService.generateClientSecret(clientId));
    }

    @ExceptionHandler(OAuthClientNotFoundException.class)
    ResponseEntity<ErrorResponse> handle(OAuthClientNotFoundException ex) {
        return ErrorResponse.getErrorResponseEntity(OAUTH_CLIENT_NOT_FOUND);
    }

    @ExceptionHandler(OAuthClientException.class)
    ResponseEntity<ErrorResponse> handle(OAuthClientException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(INVALID_OAUTH_CLIENT);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
                .map(exceptionMapper::mapToErrorDetail)
                .forEach(errorResponse.getDetails()::add);

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(OAuthScopeUnknown.class)
    ResponseEntity<ErrorResponse> handle(OAuthScopeUnknown ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(UNKNOWN_OAUTH_SCOPE);
        errorResponse.getDetails().add(new ErrorDetail("unknownScopeName", ex.getScopeName()));
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

}
