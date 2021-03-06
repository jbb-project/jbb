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

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Tags;
import org.jbb.e2e.serenity.rest.EndToEndRestStories;
import org.jbb.e2e.serenity.rest.commons.TestOAuthClient;
import org.jbb.e2e.serenity.rest.oauthclient.SetupOAuthSteps;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.jbb.lib.commons.security.OAuthScope.HEALTH_READ;

public class GetHealthRestStories extends EndToEndRestStories {

    @Steps
    HealthResourceSteps healthResourceSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

    @Test
    @WithTagValuesOf({Tags.Interface.REST, Tags.Type.SMOKE, Tags.Feature.HEALTH_CHECK, Tags.Release.VER_0_11_0})
    public void getting_health_via_api_should_be_available() {
        // when
        healthResourceSteps.get_health();

        // then
        assertRestSteps.assert_response_status(HttpStatus.OK);
        healthResourceSteps.assert_response_contains_healthy_status();
    }

    @Test
    @WithTagValuesOf({Tags.Interface.REST, Tags.Type.SMOKE, Tags.Feature.HEALTH_CHECK, Tags.Release.VER_0_12_0})
    public void client_with_health_scope_can_get_health_via_api() {
        // given
        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(HEALTH_READ);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        healthResourceSteps.get_health();

        // then
        assertRestSteps.assert_response_status(HttpStatus.OK);
        healthResourceSteps.assert_response_contains_healthy_status();
    }

    @Test
    @WithTagValuesOf({Tags.Interface.REST, Tags.Type.SMOKE, Tags.Feature.HEALTH_CHECK, Tags.Release.VER_0_12_0})
    public void client_without_health_scope_cannot_get_health_via_api() {
        // given
        TestOAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(HEALTH_READ);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        healthResourceSteps.get_health();

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }
}
