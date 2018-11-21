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
import org.jbb.e2e.serenity.rest.members.MemberPublicDto;
import org.jbb.e2e.serenity.rest.members.MemberResourceSteps;
import org.jbb.e2e.serenity.rest.members.RegistrationRequestDto;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.junit.Test;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

public class PutMemberLockoutSettingsRestStories extends EndToEndRestStories {

    @Steps
    MemberLockoutSettingsResourceSteps lockoutSettingsResourceSteps;

    @Steps
    MemberResourceSteps memberResourceSteps;

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
        register_and_mark_to_rollback("AccountTest");
        authRestSteps.include_basic_auth_header_for_every_request("AccountTest", "mysecretpass");

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
        make_rollback_after_test_case(() -> lockoutSettingsResourceSteps.put_member_lockout_settings(lockoutSettings));

        // when
        MemberLockoutSettingsDto newLockoutSettings = lockoutSettingsResourceSteps.put_member_lockout_settings(validLockoutSettings()).as(MemberLockoutSettingsDto.class);

        // then
        lockoutSettingsResourceSteps.should_contains_valid_lockout_settings();
        assertThat(newLockoutSettings).isEqualTo(validLockoutSettings());
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

    private void register_and_mark_to_rollback(String displayedName) {
        memberResourceSteps.register_member_with_success(register(displayedName));
        remove_when_rollback();
    }

    private void remove_when_rollback() {
        MemberPublicDto createdMember = then().extract().as(MemberPublicDto.class);

        make_rollback_after_test_case(
                memberResourceSteps.delete_testbed_member(createdMember.getId())
        );
    }

    private RegistrationRequestDto register(String displayedName) {
        return RegistrationRequestDto.builder()
                .username(displayedName)
                .displayedName(displayedName)
                .email(displayedName.toLowerCase() + "@gmail.com")
                .password("mysecretpass")
                .build();
    }

}
