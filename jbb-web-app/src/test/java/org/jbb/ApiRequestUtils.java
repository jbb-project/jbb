/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb;

import org.jbb.lib.restful.paging.PageDto;

import java.util.List;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

public final class ApiRequestUtils {

    public static final String ADMINISTRATOR_USERNAME = "administrator";
    public static final String ADMINISTRATOR_PSWD = "administrator";


    public static MockMvcRequestSpecification noAuthApiRequest() {
        return RestAssuredMockMvc.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }

    public static MockMvcRequestSpecification signInAdminApiRequest() {
        return signInApiRequest(ADMINISTRATOR_USERNAME, ADMINISTRATOR_PSWD);
    }

    public static MockMvcRequestSpecification signInApiRequest(String username, String password) {
        MockMvcResponse signInResponse = RestAssuredMockMvc.given()
                .param("username", username)
                .param("password", password)
                .contentType(ContentType.URLENC)
                .post("/api/v1/sign-in");
        assertThat(signInResponse.statusCode()).isEqualTo(204);

        Cookies cookies = signInResponse.detailedCookies();

        return RestAssuredMockMvc.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .cookies(cookies);
    }

    public static MockMvcRequestSpecification basicAuthUserApiRequest(String username, String password) {
        return noAuthApiRequest()
                .postProcessors(httpBasic(username, password));
    }

    public static MockMvcRequestSpecification basicAuthAdminApiRequest() {
        return basicAuthUserApiRequest(ADMINISTRATOR_USERNAME, ADMINISTRATOR_PSWD);
    }

    public static <T> T responseBodyAs(MockMvcResponse response, Class<T> responseBodyClass) {
        return response.then().extract().body().as(responseBodyClass);
    }

    public static <T> List<T> responseBodyAsListOf(MockMvcResponse response, Class<T> responseBodyClass) {
        return response.then().extract().body().as(new TypeRef<List<T>>() {
        });
    }

    public static <T> PageDto<T> responseBodyAsPageOf(MockMvcResponse response, Class<T> responseBodyClass) {
        return response.then().extract().body().as(new TypeRef<PageDto<T>>() {
        });
    }
}
