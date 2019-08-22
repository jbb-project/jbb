/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.signin;

import org.jbb.BaseIT;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.response.MockMvcResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.ApiRequestUtils.noAuthApiRequest;

public class SignInResourceIT extends BaseIT {

    @Test
    public void signInAndSignOut() {
        // when
        MockMvcResponse response = noAuthApiRequest()
                .param("username", "administrator")
                .param("password", "administrator")
                .contentType(ContentType.URLENC)
                .post("/api/v1/sign-in");

        // then
        assertThat(response.statusCode()).isEqualTo(204);

        // when
        MockMvcResponse signOutResponse = noAuthApiRequest()
                .contentType(ContentType.URLENC)
                .post("/api/v1/sign-out");

        // then
        assertThat(signOutResponse.statusCode()).isEqualTo(204);
    }

    @Test
    public void badCredentialsErrorWhenInvalidPasswordProvided() {
        // when
        MockMvcResponse response = noAuthApiRequest()
                .param("username", "administrator")
                .param("password", "badpassword")
                .contentType(ContentType.URLENC)
                .post("/api/v1/sign-in");

        // then
        assertErrorInfo(response, ErrorInfo.BAD_CREDENTIALS);
    }
}
