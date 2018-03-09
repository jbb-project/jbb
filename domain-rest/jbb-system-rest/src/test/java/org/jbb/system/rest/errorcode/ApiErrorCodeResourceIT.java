/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.errorcode;

import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.system.rest.BaseIT;
import org.junit.Test;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiErrorCodeResourceIT extends BaseIT {

    @Test
    public void getErrorCodes() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/api-error-codes");

        // then
        response.then().statusCode(200);
        ErrorCodesDto resultBody = response.then().extract().body().as(ErrorCodesDto.class);
        assertThat(resultBody.getErrorCodes()).hasSize(ErrorInfo.values().length);
    }

}