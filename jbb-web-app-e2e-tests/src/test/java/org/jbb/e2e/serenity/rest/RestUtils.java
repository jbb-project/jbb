/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import org.jbb.e2e.serenity.Utils;
import org.jbb.e2e.serenity.rest.commons.BasicAuth;

public final class RestUtils {

    public static RequestSpecification prepareApiRequest() {
        RequestSpecification request = SerenityRest.given()
            .contentType(ContentType.JSON)
            .baseUri(Utils.base_url())
            .accept(ContentType.JSON);

        BasicAuth basicAuth = Serenity.sessionVariableCalled("Auth");
        if (basicAuth != null) {
            request = request.auth().preemptive()
                .basic(basicAuth.getUsername(), basicAuth.getPassword());
        }
        return request;
    }

    public static void setBasicAuth(String username, String password) {
        Serenity.setSessionVariable("Auth").to(new BasicAuth(username, password));
    }

    public static void cleanBasicAuth() {
        Serenity.setSessionVariable("Auth").to(null);
    }

}
