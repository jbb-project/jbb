/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.profile;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.e2e.serenity.rest.RestUtils;
import org.jbb.e2e.serenity.rest.commons.AssertRestSteps;
import org.jbb.e2e.serenity.rest.commons.ErrorDetailDto;

import io.restassured.response.Response;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberProfileResourceSteps extends ScenarioSteps {

    public static final String V1_MEMBERS_PROFILE = "api/v1/members/{memberId}/profile";
    public static final String MEMBER_ID = "memberId";

    @Steps
    AssertRestSteps assertRestSteps;

    @Step
    public Response get_member_profile(String memberId) {
        return RestUtils.prepareApiRequest()
                .basePath(V1_MEMBERS_PROFILE)
                .pathParam(MEMBER_ID, memberId)
                .when()
                .get()
                .andReturn();
    }

    @Step
    public Response put_member_profile(String memberId, UpdateProfileDto updateProfileDto) {
        return RestUtils.prepareApiRequest()
                .basePath(V1_MEMBERS_PROFILE)
                .pathParam(MEMBER_ID, memberId)
                .when()
                .body(updateProfileDto)
                .put()
                .andReturn();
    }

    @Step
    public void profile_should_contains_displayed_name(String displayedName) {
        ProfileDto profileDto = then().extract().response().as(ProfileDto.class);
        assertThat(profileDto.getDisplayedName()).isEqualTo(displayedName);
    }

    @Step
    public void should_contain_error_detail_about_empty_displayed_name() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("displayedName")
                        .message("must not be empty").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_invalid_displayed_name_length() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("displayedName")
                        .message("size must be between 3 and 64").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_busy_displayed_name() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("displayedName")
                        .message("This displayed name is already taken").build()
        );
    }
}
