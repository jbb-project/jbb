/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.formatsettings;

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

import static org.jbb.lib.commons.security.OAuthScope.FORMAT_SETTINGS_READ;
import static org.jbb.lib.commons.security.OAuthScope.FORMAT_SETTINGS_READ_WRITE;

public class GetFormatSettingsRestStories extends EndToEndRestStories {

    @Steps
    FormatSettingsResourceSteps formatSettingsResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void guest_can_get_format_settings_via_api() {
        // when
        formatSettingsResourceSteps.get_format_settings();

        // then
        formatSettingsResourceSteps.should_contains_format_settings();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void member_can_get_format_settings_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.sign_in_for_every_request(member);

        // when
        formatSettingsResourceSteps.get_format_settings();

        // then
        formatSettingsResourceSteps.should_contains_format_settings();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.BOARD_SETTINGS, Release.VER_0_11_0})
    public void administrator_can_get_format_settings_via_api() {
        // given
        authRestSteps.sign_in_as_admin_for_every_request();

        // when
        formatSettingsResourceSteps.get_format_settings();

        // then
        formatSettingsResourceSteps.should_contains_format_settings();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.BOARD_SETTINGS, Release.VER_0_12_0})
    public void client_with_format_settings_read_scope_can_get_format_settings_via_api() {
        // given
        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(FORMAT_SETTINGS_READ);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        formatSettingsResourceSteps.get_format_settings();

        // then
        formatSettingsResourceSteps.should_contains_format_settings();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.BOARD_SETTINGS, Release.VER_0_12_0})
    public void client_with_format_settings_write_scope_can_get_format_settings_via_api() {
        // given
        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(FORMAT_SETTINGS_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        formatSettingsResourceSteps.get_format_settings();

        // then
        formatSettingsResourceSteps.should_contains_format_settings();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.BOARD_SETTINGS, Release.VER_0_12_0})
    public void client_without_format_settings_scopes_cannot_get_format_settings_via_api() {
        // given
        TestOAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(FORMAT_SETTINGS_READ, FORMAT_SETTINGS_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        formatSettingsResourceSteps.get_format_settings();

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

}
