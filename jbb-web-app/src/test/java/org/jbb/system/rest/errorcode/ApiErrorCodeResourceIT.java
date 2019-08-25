/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.errorcode;

import org.jbb.BaseIT;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;

import io.restassured.module.mockmvc.response.MockMvcResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.ApiRequestUtils.noAuthApiRequest;
import static org.jbb.ApiRequestUtils.responseBodyAs;

public class ApiErrorCodeResourceIT extends BaseIT {

    @Test
    public void getErrorCodes() {
        // when
        MockMvcResponse response = noAuthApiRequest().get("/api/v1/api-error-codes");

        // then
        response.then().statusCode(200);
        ErrorCodesDto responseBody = responseBodyAs(response, ErrorCodesDto.class);
        assertThat(responseBody.getErrorCodes()).hasSize(ErrorInfo.values().length);
    }

}