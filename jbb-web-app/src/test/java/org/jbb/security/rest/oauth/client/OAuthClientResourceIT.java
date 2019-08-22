/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.oauth.client;

import com.google.common.collect.Sets;

import org.jbb.BaseIT;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.paging.PageDto;
import org.jbb.security.api.oauth.GrantType;
import org.junit.Test;

import io.restassured.module.mockmvc.response.MockMvcResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.ApiRequestUtils.basicAuthAdminApiRequest;
import static org.jbb.ApiRequestUtils.noAuthApiRequest;
import static org.jbb.ApiRequestUtils.responseBodyAs;
import static org.jbb.ApiRequestUtils.responseBodyAsPageOf;

public class OAuthClientResourceIT extends BaseIT {

    @Test
    public void anonymousCannotReadOAuthClients() {
        // when
        MockMvcResponse response = noAuthApiRequest().get("/api/v1/oauth-clients");

        // then
        assertErrorInfo(response, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    public void adminIsAbleToReadOAuthClients() {
        // when
        MockMvcResponse response = basicAuthAdminApiRequest().when()
                .get("/api/v1/oauth-clients");

        // then
        PageDto<OAuthClientDto> responseBody = responseBodyAsPageOf(response, OAuthClientDto.class);
    }

    @Test
    public void creatingNewOAuthClientIsPossible_andGetting() {
        // when
        MockMvcResponse response = basicAuthAdminApiRequest()
                .body(OAuthClientDto.builder()
                        .clientId("testClient")
                        .displayedName("Testing OAuth Client")
                        .description("Client for testing")
                        .grantTypes(Sets.newHashSet(GrantType.values()))
                        .scopes(Sets.newHashSet())
                        .redirectUris(Sets.newHashSet())
                        .build()
                )
                .when()
                .post("/api/v1/oauth-clients");

        // then
        SecretOAuthClientDto responseBody = responseBodyAs(response, SecretOAuthClientDto.class);
        assertThat(responseBody.getClientSecret()).isNotBlank();

        // when
        MockMvcResponse clientResponse = basicAuthAdminApiRequest().get("/api/v1/oauth-clients/testClient");

        // then
        OAuthClientDto returnedClient = responseBodyAs(clientResponse, OAuthClientDto.class);
        assertThat(returnedClient.getClientId()).isEqualTo("testClient");
        assertThat(returnedClient.getDisplayedName()).isEqualTo("Testing OAuth Client");

        // when
        MockMvcResponse foundClients = basicAuthAdminApiRequest().queryParam("clientId", "testClient").get("/api/v1/oauth-clients");

        //then
        PageDto<OAuthClientDto> clientPage = responseBodyAsPageOf(foundClients, OAuthClientDto.class);
        assertThat(clientPage.getContent()).hasSize(1);
    }

    @Test
    public void creatingNewOAuthClientWithUnknownScope_shouldFail() {
        // when
        MockMvcResponse response = basicAuthAdminApiRequest()
                .body(OAuthClientDto.builder()
                        .clientId("testClient")
                        .displayedName("Testing OAuth Client")
                        .description("Client for testing")
                        .grantTypes(Sets.newHashSet(GrantType.values()))
                        .scopes(Sets.newHashSet("aaaaa"))
                        .redirectUris(Sets.newHashSet())
                        .build()
                )
                .when()
                .post("/api/v1/oauth-clients");

        // then
        assertErrorInfo(response, ErrorInfo.UNKNOWN_OAUTH_SCOPE);
    }

    @Test
    public void creatingNewOAuthClientWithEmptyGrantTypes_shouldFail() {
        // when
        MockMvcResponse response = basicAuthAdminApiRequest()
                .body(OAuthClientDto.builder()
                        .clientId("testClient")
                        .displayedName("Testing OAuth Client")
                        .description("Client for testing")
                        .grantTypes(Sets.newHashSet())
                        .scopes(Sets.newHashSet())
                        .redirectUris(Sets.newHashSet())
                        .build()
                )
                .when()
                .post("/api/v1/oauth-clients");

        // then
        assertErrorInfo(response, ErrorInfo.INVALID_OAUTH_CLIENT);
    }

    @Test
    public void updatingNewOAuthClientIsPossible() {
        // when
        MockMvcResponse response = basicAuthAdminApiRequest()
                .body(OAuthClientDto.builder()
                        .clientId("editClient")
                        .displayedName("Testing edit OAuth Client")
                        .description("Client for testing")
                        .grantTypes(Sets.newHashSet(GrantType.values()))
                        .scopes(Sets.newHashSet())
                        .redirectUris(Sets.newHashSet())
                        .build()
                )
                .when()
                .post("/api/v1/oauth-clients");

        // then
        assertThat(response.statusCode()).isEqualTo(201);

        // when
        MockMvcResponse clientEditResponse = basicAuthAdminApiRequest()
                .body(EditOAuthClientDto.builder()
                        .displayedName("New display name")
                        .description("Client for testing")
                        .grantTypes(Sets.newHashSet(GrantType.values()))
                        .scopes(Sets.newHashSet())
                        .redirectUris(Sets.newHashSet())
                        .build())
                .put("/api/v1/oauth-clients/editClient");

        // then
        assertThat(clientEditResponse.statusCode()).isEqualTo(200);

        // when
        MockMvcResponse clientResponse = basicAuthAdminApiRequest().get("/api/v1/oauth-clients/editClient");

        //then
        OAuthClientDto client = responseBodyAs(clientResponse, OAuthClientDto.class);
        assertThat(client.getDisplayedName()).isEqualTo("New display name");
    }

    @Test
    public void updatingSecretForClient() {
        // when
        MockMvcResponse response = basicAuthAdminApiRequest()
                .body(OAuthClientDto.builder()
                        .clientId("mySecretClient")
                        .displayedName("Testing edit OAuth Client")
                        .description("Client for testing")
                        .grantTypes(Sets.newHashSet(GrantType.values()))
                        .scopes(Sets.newHashSet())
                        .redirectUris(Sets.newHashSet())
                        .build()
                )
                .when()
                .post("/api/v1/oauth-clients");

        // then
        assertThat(response.statusCode()).isEqualTo(201);

        // when
        MockMvcResponse secretRegenerateResponse = basicAuthAdminApiRequest().queryParam("action", "regenerate")
                .put("/api/v1/oauth-clients/mySecretClient/client-secret");

        // then
        assertThat(secretRegenerateResponse.statusCode()).isEqualTo(200);
        ClientSecretDto newSecret = responseBodyAs(secretRegenerateResponse, ClientSecretDto.class);
        assertThat(newSecret.getClientSecret()).isNotBlank();
    }

    @Test
    public void deletingOAuthClient() {
        // when
        MockMvcResponse response = basicAuthAdminApiRequest()
                .body(OAuthClientDto.builder()
                        .clientId("clientToDelete")
                        .displayedName("Testing edit OAuth Client")
                        .description("Client for testing")
                        .grantTypes(Sets.newHashSet(GrantType.values()))
                        .scopes(Sets.newHashSet())
                        .redirectUris(Sets.newHashSet())
                        .build()
                )
                .when()
                .post("/api/v1/oauth-clients");

        // then
        assertThat(response.statusCode()).isEqualTo(201);

        // when
        MockMvcResponse clientDeleteResponse = basicAuthAdminApiRequest()
                .delete("/api/v1/oauth-clients/clientToDelete");

        // then
        assertThat(clientDeleteResponse.statusCode()).isEqualTo(204);

        // when
        MockMvcResponse clientResponse = basicAuthAdminApiRequest().get("/api/v1/oauth-clients/clientToDelete");

        //then
        assertErrorInfo(clientResponse, ErrorInfo.OAUTH_CLIENT_NOT_FOUND);
    }
}
