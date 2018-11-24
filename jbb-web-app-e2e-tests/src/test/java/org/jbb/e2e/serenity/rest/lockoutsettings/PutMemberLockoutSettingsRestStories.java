/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.lockoutsettings;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.lib.commons.security.OAuthScope.LOCKOUT_SETTINGS_READ_WRITE;

public class PutMemberLockoutSettingsRestStories extends EndToEndRestStories {

    @Steps
    MemberLockoutSettingsResourceSteps lockoutSettingsResourceSteps;

    @Steps
    SetupMemberSteps setupMemberSteps;

    @Steps
    SetupOAuthSteps setupOAuthSteps;

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.MEMBER_LOCKOUT, Release.VER_0_11_0})
    public void guest_cant_put_lockout_settings_via_api() {
        // when
        lockoutSettingsResourceSteps.put_member_lockout_settings(validLockoutSettings());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.MEMBER_LOCKOUT, Release.VER_0_11_0})
    public void member_cant_put_lockout_settings_via_api() {
        // given
        TestMember member = setupMemberSteps.create_member();
        make_rollback_after_test_case(setupMemberSteps.delete_member(member));
        authRestSteps.include_basic_auth_header_for_every_request(member);

        // when
        lockoutSettingsResourceSteps.put_member_lockout_settings(validLockoutSettings());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.MEMBER_LOCKOUT, Release.VER_0_11_0})
    public void administrator_can_put_lockout_settings_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        MemberLockoutSettingsDto lockoutSettings = lockoutSettingsResourceSteps.get_member_lockout_settings().as(MemberLockoutSettingsDto.class);
        make_rollback_after_test_case(restore(lockoutSettings));

        // when
        MemberLockoutSettingsDto newLockoutSettings = lockoutSettingsResourceSteps.put_member_lockout_settings(validLockoutSettings()).as(MemberLockoutSettingsDto.class);

        // then
        lockoutSettingsResourceSteps.should_contains_valid_lockout_settings();
        assertThat(newLockoutSettings).isEqualTo(validLockoutSettings());
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.MEMBER_LOCKOUT, Release.VER_0_12_0})
    public void client_with_lockout_settings_write_scope_can_put_lockout_settings_via_api() {
        // given
        TestOAuthClient client = setupOAuthSteps.create_client_with_scope(LOCKOUT_SETTINGS_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        MemberLockoutSettingsDto lockoutSettings = lockoutSettingsResourceSteps.get_member_lockout_settings().as(MemberLockoutSettingsDto.class);
        make_rollback_after_test_case(restore(lockoutSettings));

        // when
        lockoutSettingsResourceSteps.put_member_lockout_settings(validLockoutSettings());

        // then
        lockoutSettingsResourceSteps.should_contains_valid_lockout_settings();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.SMOKE, Feature.MEMBER_LOCKOUT, Release.VER_0_12_0})
    public void client_withput_lockout_settings_write_scope_cannot_put_lockout_settings_via_api() {
        // given
        TestOAuthClient client = setupOAuthSteps.create_client_with_all_scopes_except(LOCKOUT_SETTINGS_READ_WRITE);
        make_rollback_after_test_case(setupOAuthSteps.delete_oauth_client(client));
        authRestSteps.authorize_every_request_with_oauth_client(client);

        // when
        lockoutSettingsResourceSteps.put_member_lockout_settings(validLockoutSettings());

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_11_0})
    public void administrator_cant_put_lockout_settings_with_null_enabled_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        MemberLockoutSettingsDto newLockoutSettings = validLockoutSettings();
        newLockoutSettings.setLockingEnabled(null);

        // when
        lockoutSettingsResourceSteps.put_member_lockout_settings(newLockoutSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_LOCKOUT_SETTINGS);
        lockoutSettingsResourceSteps.should_contain_error_detail_about_null_locking_enabled();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_11_0})
    public void administrator_cant_put_lockout_settings_with_null_failed_attempts_threshold_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        MemberLockoutSettingsDto newLockoutSettings = validLockoutSettings();
        newLockoutSettings.setFailedAttemptsThreshold(null);

        // when
        lockoutSettingsResourceSteps.put_member_lockout_settings(newLockoutSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_LOCKOUT_SETTINGS);
        lockoutSettingsResourceSteps.should_contain_error_detail_about_null_failed_attempts_threshold();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_11_0})
    public void administrator_cant_put_lockout_settings_with_negative_failed_attempts_threshold_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        MemberLockoutSettingsDto newLockoutSettings = validLockoutSettings();
        newLockoutSettings.setFailedAttemptsThreshold(-1);

        // when
        lockoutSettingsResourceSteps.put_member_lockout_settings(newLockoutSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_LOCKOUT_SETTINGS);
        lockoutSettingsResourceSteps.should_contain_error_detail_about_not_positive_failed_attempts_threshold();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_11_0})
    public void administrator_cant_put_lockout_settings_with_zero_failed_attempts_threshold_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        MemberLockoutSettingsDto newLockoutSettings = validLockoutSettings();
        newLockoutSettings.setFailedAttemptsThreshold(0);

        // when
        lockoutSettingsResourceSteps.put_member_lockout_settings(newLockoutSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_LOCKOUT_SETTINGS);
        lockoutSettingsResourceSteps.should_contain_error_detail_about_not_positive_failed_attempts_threshold();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_11_0})
    public void administrator_cant_put_lockout_settings_with_null_failed_sign_in_attempts_expiration_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        MemberLockoutSettingsDto newLockoutSettings = validLockoutSettings();
        newLockoutSettings.setFailedSignInAttemptsExpirationMinutes(null);

        // when
        lockoutSettingsResourceSteps.put_member_lockout_settings(newLockoutSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_LOCKOUT_SETTINGS);
        lockoutSettingsResourceSteps.should_contain_error_detail_about_null_failed_sign_in_attempts_expiration();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_11_0})
    public void administrator_cant_put_lockout_settings_with_negative_failed_sign_in_attempts_expiration_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        MemberLockoutSettingsDto newLockoutSettings = validLockoutSettings();
        newLockoutSettings.setFailedSignInAttemptsExpirationMinutes(-1L);

        // when
        lockoutSettingsResourceSteps.put_member_lockout_settings(newLockoutSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_LOCKOUT_SETTINGS);
        lockoutSettingsResourceSteps.should_contain_error_detail_about_not_positive_failed_sign_in_attempts_expiration();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_11_0})
    public void administrator_cant_put_lockout_settings_with_zero_failed_sign_in_attempts_expiration_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        MemberLockoutSettingsDto newLockoutSettings = validLockoutSettings();
        newLockoutSettings.setFailedSignInAttemptsExpirationMinutes(0L);

        // when
        lockoutSettingsResourceSteps.put_member_lockout_settings(newLockoutSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_LOCKOUT_SETTINGS);
        lockoutSettingsResourceSteps.should_contain_error_detail_about_not_positive_failed_sign_in_attempts_expiration();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_11_0})
    public void administrator_cant_put_lockout_settings_with_null_lockout_duration_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        MemberLockoutSettingsDto newLockoutSettings = validLockoutSettings();
        newLockoutSettings.setLockoutDurationMinutes(null);

        // when
        lockoutSettingsResourceSteps.put_member_lockout_settings(newLockoutSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_LOCKOUT_SETTINGS);
        lockoutSettingsResourceSteps.should_contain_error_detail_about_null_lockout_duration();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_11_0})
    public void administrator_cant_put_lockout_settings_with_negative_lockout_duration_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        MemberLockoutSettingsDto newLockoutSettings = validLockoutSettings();
        newLockoutSettings.setLockoutDurationMinutes(-1L);

        // when
        lockoutSettingsResourceSteps.put_member_lockout_settings(newLockoutSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_LOCKOUT_SETTINGS);
        lockoutSettingsResourceSteps.should_contain_error_detail_about_not_positive_lockout_duration();
    }

    @Test
    @WithTagValuesOf({Interface.REST, Type.REGRESSION, Feature.MEMBER_LOCKOUT, Release.VER_0_11_0})
    public void administrator_cant_put_lockout_settings_with_zero_lockout_duration_via_api() {
        // given
        authRestSteps.include_admin_basic_auth_header_for_every_request();

        MemberLockoutSettingsDto newLockoutSettings = validLockoutSettings();
        newLockoutSettings.setLockoutDurationMinutes(0L);

        // when
        lockoutSettingsResourceSteps.put_member_lockout_settings(newLockoutSettings);

        // then
        assertRestSteps.assert_response_error_info(ErrorInfo.INVALID_LOCKOUT_SETTINGS);
        lockoutSettingsResourceSteps.should_contain_error_detail_about_not_positive_lockout_duration();
    }

    private MemberLockoutSettingsDto validLockoutSettings() {
        return MemberLockoutSettingsDto.builder()
                .lockingEnabled(true)
                .lockoutDurationMinutes(120L)
                .failedAttemptsThreshold(5)
                .failedSignInAttemptsExpirationMinutes(10L)
                .build();
    }

    private RollbackAction restore(MemberLockoutSettingsDto memberLockoutSettingsDto) {
        return () -> {
            authRestSteps.include_admin_basic_auth_header_for_every_request();
            lockoutSettingsResourceSteps.put_member_lockout_settings(memberLockoutSettingsDto);
            authRestSteps.remove_authorization_headers_from_request();
        };
    }

}
