/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.swagger;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.e2e.serenity.rest.RestUtils;

import io.restassured.response.Response;

public class SwaggerResourceSteps extends ScenarioSteps {

    public static final String SWAGGER = "api/swagger-v2";

    @Step
    public Response get_swagger_api_docs() {
        return RestUtils.prepareApiRequest()
                .basePath(SWAGGER)
                .when()
                .get()
                .andReturn();
    }
}
