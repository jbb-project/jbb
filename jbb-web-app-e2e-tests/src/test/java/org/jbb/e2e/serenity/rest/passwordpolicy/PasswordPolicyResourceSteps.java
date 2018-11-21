/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.passwordpolicy;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.e2e.serenity.rest.RestUtils;
import org.jbb.e2e.serenity.rest.commons.AssertRestSteps;
import org.jbb.e2e.serenity.rest.commons.ErrorDetailDto;

import io.restassured.response.Response;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

public class PasswordPolicyResourceSteps extends ScenarioSteps {

    public static final String V1_PASSWORD_POLICY = "api/v1/password-policy";

    @Steps
    AssertRestSteps assertRestSteps;

    @Step
    public Response get_password_policy() {
        return RestUtils.prepareApiRequest()
                .basePath(V1_PASSWORD_POLICY)
                .when()
                .get()
                .andReturn();
    }

    @Step
    public Response put_password_policy(PasswordPolicyDto passwordPolicyDto) {
        return RestUtils.prepareApiRequest()
                .basePath(V1_PASSWORD_POLICY)
                .when()
                .body(passwordPolicyDto)
                .put()
                .andReturn();
    }

    @Step
    public void should_contain_error_detail_about_null_minimum_length() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("minimumLength")
                        .message("must not be null").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_not_positive_minimum_length() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("minimumLength")
                        .message("must be greater than or equal to 1").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_null_maximum_length() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("maximumLength")
                        .message("must not be null").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_not_positive_maximum_length() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("maximumLength")
                        .message("must be greater than or equal to 1").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_minimum_length_greater_than_maximum() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("minimumLength")
                        .message("Minimum length of password is greater than maximum length").build()
        );
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("maximumLength")
                        .message("Minimum length of password is greater than maximum length").build()
        );
    }

    @Step
    public void should_contains_password_policy() {
        PasswordPolicyDto passwordPolicyDto = then().extract().response().as(PasswordPolicyDto.class);
        assertThat(passwordPolicyDto.getMinimumLength()).isNotNegative();
        assertThat(passwordPolicyDto.getMaximumLength()).isNotNegative();
    }
}
