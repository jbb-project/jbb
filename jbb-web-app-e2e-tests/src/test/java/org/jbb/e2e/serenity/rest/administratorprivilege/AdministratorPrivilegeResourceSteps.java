/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.administratorprivilege;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.e2e.serenity.rest.RestUtils;
import org.jbb.e2e.serenity.rest.commons.AssertRestSteps;
import org.jbb.e2e.serenity.rest.commons.ErrorDetailDto;

import io.restassured.response.Response;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

public class AdministratorPrivilegeResourceSteps extends ScenarioSteps {

    public static final String V1_ADMINISTRATOR_PRIVILEGES = "api/v1/members/{memberId}/administrator-privileges";
    public static final String MEMBER_ID = "memberId";

    @Steps
    AssertRestSteps assertRestSteps;

    @Step
    public Response get_administrator_privileges(String memberId) {
        return RestUtils.prepareApiRequest()
                .basePath(V1_ADMINISTRATOR_PRIVILEGES)
                .pathParam(MEMBER_ID, memberId)
                .when()
                .get()
                .andReturn();
    }

    @Step
    public Response put_administrator_privileges(String memberId, UpdatePrivilegesDto updatePrivilegesDto) {
        return RestUtils.prepareApiRequest()
                .basePath(V1_ADMINISTRATOR_PRIVILEGES)
                .pathParam(MEMBER_ID, memberId)
                .when()
                .body(updatePrivilegesDto)
                .put()
                .andReturn();
    }

    @Step
    public void should_contains_privileges_value() {
        PrivilegesDto privilegesDto = then().extract().response().as(PrivilegesDto.class);
        assertThat(privilegesDto.getAdministratorPrivileges()).isNotNull();
    }

    @Step
    public void should_contain_error_detail_about_null_administrator_privilege() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("administratorPrivileges")
                        .message("must not be null").build()
        );
    }

    @Step
    public void member_should_not_be_an_administrator() {
        PrivilegesDto privilegesDto = then().extract().response().as(PrivilegesDto.class);
        assertThat(privilegesDto.getAdministratorPrivileges()).isFalse();
    }

    @Step
    public void member_should_be_an_administrator() {
        PrivilegesDto privilegesDto = then().extract().response().as(PrivilegesDto.class);
        assertThat(privilegesDto.getAdministratorPrivileges()).isTrue();
    }

    @Step
    public void member_should_not_be_an_administrator(Long memberId) {
        PrivilegesDto privilegesDto = then().extract().response().as(PrivilegesDto.class);
        assertThat(privilegesDto.getAdministratorPrivileges()).isFalse();
        assertThat(privilegesDto.getMemberId()).isEqualTo(memberId);
    }

    @Step
    public void member_should_be_an_administrator(Long memberId) {
        PrivilegesDto privilegesDto = then().extract().response().as(PrivilegesDto.class);
        assertThat(privilegesDto.getAdministratorPrivileges()).isTrue();
        assertThat(privilegesDto.getMemberId()).isEqualTo(memberId);
    }

}
