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

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.e2e.serenity.rest.RestUtils;
import org.jbb.e2e.serenity.rest.commons.AssertRestSteps;
import org.jbb.e2e.serenity.rest.commons.ErrorDetailDto;
import org.springframework.http.HttpStatus;

import io.restassured.response.Response;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberLockoutSettingsResourceSteps extends ScenarioSteps {

    public static final String V1_MEMBER_LOCKOUT_SETTINGS = "api/v1/member-lockout-settings";

    @Steps
    AssertRestSteps assertRestSteps;

    @Step
    public Response get_member_lockout_settings() {
        return RestUtils.prepareApiRequest()
                .basePath(V1_MEMBER_LOCKOUT_SETTINGS)
                .when()
                .get()
                .andReturn();
    }

    @Step
    public Response put_member_lockout_settings(MemberLockoutSettingsDto lockoutSettingsDto) {
        return RestUtils.prepareApiRequest()
                .basePath(V1_MEMBER_LOCKOUT_SETTINGS)
                .when()
                .body(lockoutSettingsDto)
                .put()
                .andReturn();
    }

    @Step
    public void should_contains_valid_lockout_settings() {
        assertRestSteps.assert_response_status(HttpStatus.OK);
        MemberLockoutSettingsDto memberLockoutSettingsDto = then().extract().response().as(MemberLockoutSettingsDto.class);
        assertThat(memberLockoutSettingsDto.getFailedAttemptsThreshold()).isPositive();
        assertThat(memberLockoutSettingsDto.getFailedSignInAttemptsExpirationMinutes()).isPositive();
        assertThat(memberLockoutSettingsDto.getLockoutDurationMinutes()).isPositive();
        assertThat(memberLockoutSettingsDto.getLockingEnabled()).isNotNull();
    }

    @Step
    public void should_contain_error_detail_about_null_locking_enabled() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("lockingEnabled")
                        .message("must not be null").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_null_failed_attempts_threshold() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("failedAttemptsThreshold")
                        .message("must not be null").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_not_positive_failed_attempts_threshold() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("failedAttemptsThreshold")
                        .message("must be greater than or equal to 1").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_null_failed_sign_in_attempts_expiration() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("failedSignInAttemptsExpirationMinutes")
                        .message("must not be null").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_not_positive_failed_sign_in_attempts_expiration() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("failedSignInAttemptsExpirationMinutes")
                        .message("must be greater than or equal to 1").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_null_lockout_duration() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("lockoutDurationMinutes")
                        .message("must not be null").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_not_positive_lockout_duration() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("lockoutDurationMinutes")
                        .message("must be greater than or equal to 1").build()
        );
    }
}
