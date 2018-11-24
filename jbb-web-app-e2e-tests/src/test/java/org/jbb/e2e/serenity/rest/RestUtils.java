/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest;

import net.serenitybdd.core.Serenity;

import org.jbb.e2e.serenity.Utils;
import org.jbb.e2e.serenity.rest.commons.BasicAuth;
import org.jbb.e2e.serenity.rest.commons.OAuthClient;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.rest.SerenityRest.rest;

public final class RestUtils {

    public static RequestSpecification prepareApiRequest() {
        String accessToken = null;
        OAuthClient clientCredentials = Serenity.sessionVariableCalled("OAuthClientCredentials");
        if (clientCredentials != null) {
            String tokenResponse = given().auth()
                    .basic(clientCredentials.getClientId(),
                            clientCredentials.getClientSecret())
                    .baseUri(Utils.base_url())
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("grant_type", "client_credentials")
                    .when()
                    .post("/oauth/token")
                    .asString();
            JsonPath jsonPath = new JsonPath(tokenResponse);
            accessToken = jsonPath.getString("access_token");
        }

        RequestSpecification request = rest()
                .contentType(ContentType.JSON)
                .baseUri(Utils.base_url())
                .accept(ContentType.JSON);

        BasicAuth basicAuth = Serenity.sessionVariableCalled("Auth");
        if (basicAuth != null) {
            request = request.auth().preemptive()
                    .basic(basicAuth.getUsername(), basicAuth.getPassword());
        }
        if (accessToken != null) {
            request = request.auth().preemptive().oauth2(accessToken);
        }

        return request;
    }

    public static void setBasicAuth(String username, String password) {
        Serenity.setSessionVariable("Auth").to(new BasicAuth(username, password));
    }

    public static void setClientCredentialsOAuth(String clientId, String clientSecret) {
        Serenity.setSessionVariable("OAuthClientCredentials").to(new OAuthClient(clientId, clientSecret));
    }

    public static void cleanBasicAuth() {
        Serenity.setSessionVariable("Auth").to(null);
    }

    public static void cleanClientCredentialsOAuth() {
        Serenity.setSessionVariable("OAuthClientCredentials").to(null);
    }

}
