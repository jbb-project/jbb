/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.swagger;

import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbb.e2e.serenity.rest.RestUtils;

public class SwaggerResourceSteps extends ScenarioSteps {

    public static final String SWAGGER = "v2/api-docs";

    @Step
    public Response get_swagger_api_docs() {
        return RestUtils.prepareApiRequest()
            .basePath(SWAGGER)
            .when()
            .get()
            .andReturn();
    }
}
