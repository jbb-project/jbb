/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.health;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.e2e.serenity.rest.RestUtils;

import io.restassured.response.Response;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

public class HealthResourceSteps extends ScenarioSteps {

    public static final String V1_HEALTH = "api/v1/health";

    @Step
    public HealthDto get_health() {
        return RestUtils.prepareApiRequest()
                .basePath(V1_HEALTH)
                .when()
                .get()
                .as(HealthDto.class);
    }

    @Step
    public void assert_response_contains_healthy_status() {
        Response response = then().extract().response();
        HealthDto healthDto = response.as(HealthDto.class);

        assertThat(healthDto.getStatus()).isEqualTo("HEALTHY");
        assertThat(healthDto.getLastCheckedAt()).isNotNull();
        assertThat(healthDto.getApplicationVersion()).isNotBlank();
    }

}
