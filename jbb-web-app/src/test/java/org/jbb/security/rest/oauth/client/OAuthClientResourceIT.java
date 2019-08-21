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
    public void creatingNewOAuthClientIsPossible() {
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
    }
}
