/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.registrationsettings;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Tags.Feature;
import org.jbb.e2e.serenity.Tags.Interface;
import org.jbb.e2e.serenity.Tags.Release;
import org.jbb.e2e.serenity.Tags.Type;
import org.jbb.e2e.serenity.rest.EndToEndRestStories;
import org.jbb.e2e.serenity.rest.commons.TestMember;
import org.jbb.e2e.serenity.rest.commons.TestOAuthClient;
import org.jbb.e2e.serenity.rest.members.SetupMemberSteps;
import org.jbb.e2e.serenity.rest.oauthclient.SetupOAuthSteps;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.jbb.lib.commons.security.OAuthScope.REGISTRATION_SETTINGS_READ;
import static org.jbb.lib.commons.security.OAuthScope.REGISTRATION_SETTINGS_READ_WRITE;

public class GetRegistrationSettingsRestStories extends EndToEndRestStories {

    @Steps
    RegistrationSettingsResourceSteps registrationSettingsResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void guest_cannot_get_registration_settings_via_api() {
        // when
        registrationSettingsResourceSteps.get_registration_settings();

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void regular_member_cannot_get_registration_settings_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.include_basic_auth_header_for_every_request(member);

        // when
        registrationSettingsResourceSteps.get_registration_settings();

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void administrator_can_get_registration_settings_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        // when
        registrationSettingsResourceSteps.get_registration_settings();

        // then
        assertRestSteps.assert_response_status(HttpStatus.OK);
        registrationSettingsResourceSteps.registration_settings_should_contain_email_duplication();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_12_0})
    public void client_with_registration_settings_read_scope_can_get_registration_settings_via_api() {
        // given
        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(REGISTRATION_SETTINGS_READ);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        registrationSettingsResourceSteps.get_registration_settings();

        // then
        assertRestSteps.assert_response_status(HttpStatus.OK);
        registrationSettingsResourceSteps.registration_settings_should_contain_email_duplication();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_12_0})
    public void client_with_registration_settings_write_scope_can_get_registration_settings_via_api() {
        // given
        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(REGISTRATION_SETTINGS_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        registrationSettingsResourceSteps.get_registration_settings();

        // then
        assertRestSteps.assert_response_status(HttpStatus.OK);
        registrationSettingsResourceSteps.registration_settings_should_contain_email_duplication();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_12_0})
    public void client_without_registration_settings_scopes_cannot_get_registration_settings_via_api() {
        // given
        TestOAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(REGISTRATION_SETTINGS_READ, REGISTRATION_SETTINGS_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        registrationSettingsResourceSteps.get_registration_settings();

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

}
