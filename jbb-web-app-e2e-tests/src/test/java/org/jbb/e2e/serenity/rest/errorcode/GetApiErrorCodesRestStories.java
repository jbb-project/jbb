/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.errorcode;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Tags.Feature;
import org.jbb.e2e.serenity.Tags.Interface;
import org.jbb.e2e.serenity.Tags.Release;
import org.jbb.e2e.serenity.Tags.Type;
import org.jbb.e2e.serenity.rest.EndToEndRestStories;
import org.jbb.e2e.serenity.rest.commons.OAuthClient;
import org.jbb.e2e.serenity.rest.oauthclient.SetupOAuthSteps;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.jbb.lib.commons.security.OAuthScope.API_ERROR_CODES_READ;

public class GetApiErrorCodesRestStories extends EndToEndRestStories {

    @Steps
    ApiErrorCodeResourceSteps apiErrorCodeResourceSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REST_API, Release.VER_0_10_0})
    public void getting_api_error_code_should_be_available() {
        // when
        apiErrorCodeResourceSteps.get_api_error_codes();

        // then
        assertRestSteps.assert_response_status(HttpStatus.OK);
        assertRestSteps.assert_json_content_type();
        apiErrorCodeResourceSteps.assert_response_contains_valid_error_codes();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REST_API, Release.VER_0_12_0})
    public void client_with_api_error_code_read_scope_can_read_error_codes() {
        // given
        OAuthClient client = setupOAuthSteps.create_client_with_scope(API_ERROR_CODES_READ);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        apiErrorCodeResourceSteps.get_api_error_codes();

        // then
        assertRestSteps.assert_response_status(HttpStatus.OK);
        assertRestSteps.assert_json_content_type();
        apiErrorCodeResourceSteps.assert_response_contains_valid_error_codes();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REST_API, Release.VER_0_12_0})
    public void client_without_api_error_code_read_scope_cannot_read_error_codes() {
        // given
        OAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(API_ERROR_CODES_READ);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        apiErrorCodeResourceSteps.get_api_error_codes();

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

}
