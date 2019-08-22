/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.oauthclient;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.e2e.serenity.rest.RestUtils;
import org.jbb.e2e.serenity.rest.commons.AssertRestSteps;
import org.jbb.e2e.serenity.rest.commons.AuthRestSteps;

import io.restassured.response.Response;

public class OAuthClientResourceSteps extends ScenarioSteps {

    public static final String V1_OAUTH_CLIENTS = "api/v1/oauth-clients";

    @Steps
    AssertRestSteps assertRestSteps;

    @Steps
    AuthRestSteps authRestSteps;

    @Step
    public Response create_oauth_client(OAuthClientDto oAuthClientDto) {
        return RestUtils.prepareApiRequest()
                .basePath(V1_OAUTH_CLIENTS)
                .body(oAuthClientDto)
                .when()
                .post()
                .andReturn();
    }

    @Step
    public Response delete_oauth_client(String clientId) {
        return RestUtils.prepareApiRequest()
                .basePath(V1_OAUTH_CLIENTS + "/{clientId}")
                .pathParam("clientId", clientId)
                .when()
                .delete()
                .andReturn();
    }

}
