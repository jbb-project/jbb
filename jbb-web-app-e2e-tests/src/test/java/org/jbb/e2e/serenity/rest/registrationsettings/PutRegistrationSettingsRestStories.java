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
import org.jbb.e2e.serenity.rest.members.MemberResourceSteps;
import org.jbb.e2e.serenity.rest.members.RegistrationRequestDto;
import org.jbb.e2e.serenity.rest.members.SetupMemberSteps;
import org.jbb.e2e.serenity.rest.oauthclient.SetupOAuthSteps;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.jbb.lib.commons.security.OAuthScope.REGISTRATION_SETTINGS_READ_WRITE;

public class PutRegistrationSettingsRestStories extends EndToEndRestStories {

    @Steps
    RegistrationSettingsResourceSteps registrationSettingsResourceSteps;

    @Steps
    MemberResourceSteps memberResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void guest_cant_put_registration_settings_via_api() {
        // when
        registrationSettingsResourceSteps.put_registration_settings(validRegistrationSettings());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void regular_member_cant_put_registration_settings_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.include_basic_auth_header_for_every_request(member);

        // when
        registrationSettingsResourceSteps.put_registration_settings(validRegistrationSettings());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_12_0})
    public void client_with_registration_settings_write_scope_can_put_registration_settings_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        RegistrationSettingsDto currentSettings = registrationSettingsResourceSteps.get_registration_settings().as(RegistrationSettingsDto.class);
        make_rollback_after_test_case(restore(currentSettings));

        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(REGISTRATION_SETTINGS_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        registrationSettingsResourceSteps.put_registration_settings(validRegistrationSettings());

        // then
        registrationSettingsResourceSteps.registration_settings_should_contain_email_duplication();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_12_0})
    public void client_without_registration_settings_write_scope_cannot_put_registration_settings_via_api() {
        // given
        TestOAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(REGISTRATION_SETTINGS_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        registrationSettingsResourceSteps.put_registration_settings(validRegistrationSettings());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void put_registration_settings_without_email_duplication_allowed_value_is_impossible() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        RegistrationSettingsDto registrationSettings = validRegistrationSettings();
        registrationSettings.setEmailDuplicationAllowed(null);

        // when
        registrationSettingsResourceSteps.put_registration_settings(registrationSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.VALIDATION_ERROR);
        registrationSettingsResourceSteps.should_contain_error_detail_about_null_email_duplication_allowed();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void put_registration_settings_with_invalid_email_duplication_allowed_value_is_impossible() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        RegistrationSettingsDto registrationSettings = validRegistrationSettings();
        registrationSettings.setEmailDuplicationAllowed("tru");

        // when
        registrationSettingsResourceSteps.put_registration_settings(registrationSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_FORMAT_PROPERTY);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void when_email_duplication_is_allowed_via_api_then_many_members_can_use_the_same_email() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        RegistrationSettingsDto currentSettings = registrationSettingsResourceSteps.get_registration_settings().as(RegistrationSettingsDto.class);
        make_rollback_after_test_case(restore(currentSettings));

        RegistrationSettingsDto registrationSettings = validRegistrationSettings();
        registrationSettings.setEmailDuplicationAllowed(true);

        // when
        registrationSettingsResourceSteps.put_registration_settings(registrationSettings);

        // then
        registrationSettingsResourceSteps.email_duplication_should_be_enabled();

        // when
        TestMember firstMember = setupMemberSteps.create_member_with_email("testbed@jbb.com");
        TestMember secondMember = setupMemberSteps.create_member_with_email("testbed@jbb.com");

        make_rollback_after_test_case(setupMemberSteps.delete_member(firstMember.getMemberId()));
        make_rollback_after_test_case(setupMemberSteps.delete_member(secondMember.getMemberId()));

        // then
        assertRestSteps.assert_response_status(HttpStatus.CREATED);
    }


    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.REGISTRATION, Release.VER_0_10_0})
    public void when_email_duplication_is_disallowed_via_api_then_many_members_cannot_use_the_same_email() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();
        RegistrationSettingsDto currentSettings = registrationSettingsResourceSteps.get_registration_settings().as(RegistrationSettingsDto.class);
        make_rollback_after_test_case(restore(currentSettings));

        RegistrationSettingsDto registrationSettings = validRegistrationSettings();
        registrationSettings.setEmailDuplicationAllowed(false);

        // when
        registrationSettingsResourceSteps.put_registration_settings(registrationSettings);

        // then
        registrationSettingsResourceSteps.email_duplication_should_be_disabled();

        // when
        TestMember member = setupMemberSteps.create_member_with_email("testbed@jbb.com.pl");
        make_rollback_after_test_case(setupMemberSteps.delete_member(member.getMemberId()));

        memberResourceSteps.post_member(registerWithEmail("testbed@jbb.com.pl"));

        // then
        memberResourceSteps.should_contain_error_detail_about_busy_email();
    }

    private RegistrationSettingsDto validRegistrationSettings() {
        return RegistrationSettingsDto.builder().emailDuplicationAllowed(false).build();
    }

    private RegistrationRequestDto registerWithEmail(String email) {
        return RegistrationRequestDto.builder()
                .username("anna")
                .displayedName("Anna")
                .email(email)
                .password("mypass")
                .build();
    }

    private RollbackAction restore(RegistrationSettingsDto registrationRequestDto) {
        return () -> {
            authRestSteps.include_admin_basic_auth_header_for_every_request();
            registrationSettingsResourceSteps.put_registration_settings(registrationRequestDto);
            authRestSteps.remove_authorization_headers_from_request();
        };
    }


}
