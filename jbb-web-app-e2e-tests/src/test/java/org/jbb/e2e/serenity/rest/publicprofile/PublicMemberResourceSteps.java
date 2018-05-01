/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.publicprofile;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.e2e.serenity.rest.RestUtils;

import io.restassured.response.Response;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

public class PublicMemberResourceSteps extends ScenarioSteps {

    public static final String V1_MEMBERS_PUBLIC_PROFILE = "api/v1/members/{memberId}/public-profile";
    public static final String MEMBER_ID = "memberId";

    @Step
    public Response get_public_profile(String memberId) {
        return RestUtils.prepareApiRequest()
                .basePath(V1_MEMBERS_PUBLIC_PROFILE)
                .pathParam(MEMBER_ID, memberId)
                .when()
                .get()
                .andReturn();
    }

    @Step
    public void should_contain_displayed_name(String displayedName) {
        ProfilePublicDto profilePublicDto = then().extract().response().as(ProfilePublicDto.class);
        assertThat(profilePublicDto.getDisplayedName()).isEqualTo(displayedName);
    }

    @Step
    public void should_contain_join_date_time() {
        ProfilePublicDto profilePublicDto = then().extract().response().as(ProfilePublicDto.class);
        assertThat(profilePublicDto.getJoinDateTime()).isNotNull();
    }
}
