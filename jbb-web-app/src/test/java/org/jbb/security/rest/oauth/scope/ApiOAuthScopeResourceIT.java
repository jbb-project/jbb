/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.oauth.scope;

import org.apache.commons.lang3.StringUtils;
import org.jbb.BaseIT;
import org.junit.Test;

import io.restassured.module.mockmvc.response.MockMvcResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.ApiRequestUtils.noAuthApiRequest;
import static org.jbb.ApiRequestUtils.responseBody;

public class ApiOAuthScopeResourceIT extends BaseIT {

    @Test
    public void should_return_all_oauth_scopes() {
        // when
        MockMvcResponse response = noAuthApiRequest().get("/api/v1/api-oauth-scopes");

        // then
        response.then().statusCode(200);
        OAuthScopesDto resultBody = responseBody(response, OAuthScopesDto.class);

        assertThat(resultBody.getScopes()).isNotEmpty();
        assertThat(resultBody.getScopes()).allSatisfy(scope -> StringUtils.isNotBlank(scope.getName()));
        assertThat(resultBody.getScopes()).allSatisfy(scope -> StringUtils.isNotBlank(scope.getDescription()));
    }

}
