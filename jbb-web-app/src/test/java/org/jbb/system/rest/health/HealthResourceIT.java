/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.health;

import org.jbb.BaseIT;
import org.jbb.system.api.health.HealthStatus;
import org.junit.Test;

import io.restassured.module.mockmvc.response.MockMvcResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.ApiRequestUtils.noAuthApiRequest;
import static org.jbb.ApiRequestUtils.responseBodyAs;

public class HealthResourceIT extends BaseIT {

    @Test
    public void getHealthStatus() {
        // when
        MockMvcResponse response = noAuthApiRequest().get("/api/v1/health");

        // then
        response.then().statusCode(200);
        HealthDto responseBody = responseBodyAs(response, HealthDto.class);
        assertThat(responseBody.getStatus()).isEqualTo(HealthStatus.HEALTHY);
    }

}