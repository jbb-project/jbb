/*
 * Copyright (C) 2019 the original author or authors.
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
    public static final String ADMIN_DISPLAYED_NAME = "Administrator";

    @Step
    public void sign_in_as_admin_for_every_request() {
        sign_in_for_every_request(ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    @Step
    public void sign_in_for_every_request(String username, String password) {
        RestUtils.setUserPassword(username, password);
        RestUtils.cleanClientCredentialsOAuth();
        RestUtils.cleanBasicAuth();
    }

    @Step
    public void sign_in_for_every_request(TestMember member) {
        RestUtils.setUserPassword(member.getUsername(), member.getPassword());
        RestUtils.cleanClientCredentialsOAuth();
        RestUtils.cleanBasicAuth();
    }

    @Step
    public void authorize_every_request_with_oauth_client(String clientId, String clientSecret) {
        RestUtils.setClientCredentialsOAuth(clientId, clientSecret);
        RestUtils.cleanBasicAuth();
        RestUtils.cleanUserPassword();
    }

    @Step
    public void authorize_every_request_with_oauth_client(TestOAuthClient client) {
        RestUtils.setClientCredentialsOAuth(client.getClientId(), client.getClientSecret());
        RestUtils.cleanBasicAuth();
        RestUtils.cleanUserPassword();
    }

    @Step
    public void remove_sign_in_authentication_from_request() {
        RestUtils.cleanUserPassword();
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
        RestUtils.cleanUserPassword();
    }

}
