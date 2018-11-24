/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.commons;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.e2e.serenity.rest.RestUtils;

public class AuthRestSteps extends ScenarioSteps {
    public static final String ADMIN_USERNAME = "administrator";
    public static final String ADMIN_PASSWORD = "administrator";

    @Step
    public void include_admin_basic_auth_header_for_every_request() {
        include_basic_auth_header_for_every_request(ADMIN_USERNAME, ADMIN_PASSWORD);
        RestUtils.cleanClientCredentialsOAuth();
    }

    @Step
    public void include_basic_auth_header_for_every_request(String username, String password) {
        RestUtils.setBasicAuth(username, password);
        RestUtils.cleanClientCredentialsOAuth();
    }

    @Step
    public void include_basic_auth_header_for_every_request(TestMember member) {
        RestUtils.setBasicAuth(member.getUsername(), member.getPassword());
        RestUtils.cleanClientCredentialsOAuth();
    }

    @Step
    public void authorize_every_request_with_oauth_client(String clientId, String clientSecret) {
        RestUtils.setClientCredentialsOAuth(clientId, clientSecret);
        RestUtils.cleanBasicAuth();
    }

    @Step
    public void authorize_every_request_with_oauth_client(OAuthClient client) {
        RestUtils.setClientCredentialsOAuth(client.getClientId(), client.getClientSecret());
        RestUtils.cleanBasicAuth();
    }

    @Step
    public void remove_basic_auth_header_from_request() {
        RestUtils.cleanBasicAuth();
    }

    @Step
    public void remove_oauth_client_authentication_from_request() {
        RestUtils.cleanClientCredentialsOAuth();
    }

    @Step
    public void remove_authorization_headers_from_request() {
        RestUtils.cleanBasicAuth();
        RestUtils.cleanClientCredentialsOAuth();
    }

}
