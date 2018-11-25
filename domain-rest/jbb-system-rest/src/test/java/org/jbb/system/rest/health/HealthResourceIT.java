/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.health;

import org.jbb.system.api.health.HealthCheckService;
import org.jbb.system.api.health.HealthResult;
import org.jbb.system.api.health.HealthStatus;
import org.jbb.system.rest.BaseIT;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static org.assertj.core.api.Assertions.assertThat;

public class HealthResourceIT extends BaseIT {

    @Autowired
    HealthCheckService healthCheckServiceMock;

    @Test
    public void getHealthStatus_whenAppIsHealthy() {
        // given
        LocalDateTime lastCheckTime = LocalDateTime.now();
        BDDMockito.given(healthCheckServiceMock.getHealth()).willReturn(HealthResult.builder()
                .status(HealthStatus.HEALTHY)
                .lastCheckedAt(lastCheckTime)
                .build());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/health");

        // then
        response.then().statusCode(200);
        HealthDto resultBody = response.then().extract().body().as(HealthDto.class);
        assertThat(resultBody.getStatus()).isEqualTo(HealthStatus.HEALTHY);
        assertThat(resultBody.getApplicationVersion()).isNotBlank();
        assertThat(resultBody.getLastCheckedAt()).isEqualTo(lastCheckTime);
    }

    @Test
    public void getHealthStatus_whenAppIsUnhealthy() {
        // given
        LocalDateTime lastCheckTime = LocalDateTime.now();
        BDDMockito.given(healthCheckServiceMock.getHealth()).willReturn(HealthResult.builder()
                .status(HealthStatus.UNHEALTHY)
                .lastCheckedAt(lastCheckTime)
                .build());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/health");

        // then
        response.then().statusCode(500);
        HealthDto resultBody = response.then().extract().body().as(HealthDto.class);
        assertThat(resultBody.getStatus()).isEqualTo(HealthStatus.UNHEALTHY);
        assertThat(resultBody.getApplicationVersion()).isNotBlank();
        assertThat(resultBody.getLastCheckedAt()).isEqualTo(lastCheckTime);
    }

}