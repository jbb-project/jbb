/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.members;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbb.e2e.serenity.rest.RestUtils;
import org.jbb.e2e.serenity.rest.commons.AssertRestSteps;
import org.jbb.e2e.serenity.rest.commons.AuthRestSteps;
import org.jbb.e2e.serenity.rest.commons.ErrorDetailDto;
import org.jbb.e2e.serenity.rest.commons.PageDto;
import org.jbb.e2e.serenity.web.EndToEndWebStories.RollbackAction;
import org.springframework.http.HttpStatus;

public class MemberResourceSteps extends ScenarioSteps {

    public static final String V1_MEMBERS = "api/v1/members";

    @Steps
    AssertRestSteps assertRestSteps;

    @Steps
    AuthRestSteps authRestSteps;

    public RollbackAction delete_testbed_member(Long memberId) {
        return () -> {
            authRestSteps.include_admin_basic_auth_header_for_every_request();
            delete_member(memberId.toString());
        };
    }

    @Step
    public PageDto<MemberPublicDto> get_with_displayed_name(String displayedName) {
        return RestUtils.prepareApiRequest()
            .basePath(V1_MEMBERS)
            .param("displayedName", displayedName)
            .when()
            .get()
            .as(PageDto.class);
    }

    @Step
    public Response get_member_page_with_page_number(String page) {
        return RestUtils.prepareApiRequest()
            .basePath(V1_MEMBERS)
            .param("page", page)
            .when()
            .get()
            .andReturn();
    }

    @Step
    public Response get_member_page_with_page_size(String pageSize) {
        return RestUtils.prepareApiRequest()
            .basePath(V1_MEMBERS)
            .param("pageSize", pageSize)
            .when()
            .get()
            .andReturn();
    }

    @Step
    public Response register_member_with_success(RegistrationRequestDto registrationRequestDto) {
        Response response = post_member(registrationRequestDto);
        assertRestSteps.assert_response_status(HttpStatus.CREATED);
        return response;
    }

    @Step
    public Response post_member(RegistrationRequestDto registrationRequestDto) {
        return RestUtils.prepareApiRequest()
            .basePath(V1_MEMBERS)
            .body(registrationRequestDto)
            .when()
            .post()
            .andReturn();
    }

    @Step
    public Response delete_member(String memberId) {
        return RestUtils.prepareApiRequest()
            .basePath(V1_MEMBERS + "/{memberId}")
            .pathParam("memberId", memberId)
            .when()
            .delete()
            .andReturn();
    }

    @Step
    public void created_member_should_have_id() {
        MemberPublicDto memberPublicDto = then().extract().as(MemberPublicDto.class);

        assertThat(memberPublicDto.getId()).isNotNull();
        assertThat(memberPublicDto.getDisplayedName()).isNotBlank();
        assertThat(memberPublicDto.getJoinDateTime()).isNotNull();
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

    @Step
    public void should_contain_error_detail_about_empty_email() {
        assertRestSteps.assert_response_error_detail_exists(
            ErrorDetailDto.builder()
                .name("email")
                .message("must not be empty").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_invalid_email() {
        assertRestSteps.assert_response_error_detail_exists(
            ErrorDetailDto.builder()
                .name("email")
                .message("must be a well-formed email address").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_busy_email() {
        assertRestSteps.assert_response_error_detail_exists(
            ErrorDetailDto.builder()
                .name("email")
                .message("This e-mail is already used by another member").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_empty_username() {
        assertRestSteps.assert_response_error_detail_exists(
            ErrorDetailDto.builder()
                .name("username")
                .message("must not be empty").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_white_characters_in_username() {
        assertRestSteps.assert_response_error_detail_exists(
            ErrorDetailDto.builder()
                .name("username")
                .message("Username cannot contain spaces and other white characters").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_invalid_username_size() {
        assertRestSteps.assert_response_error_detail_exists(
            ErrorDetailDto.builder()
                .name("username")
                .message("size must be between 3 and 20").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_busy_username() {
        assertRestSteps.assert_response_error_detail_exists(
            ErrorDetailDto.builder()
                .name("username")
                .message("This username is already taken").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_invalid_password_length() {
        assertRestSteps.assert_response_error_detail_exists(
            ErrorDetailDto.builder()
                .name("password")
                .message("Password has incorrect length (min: 4, max: 16)").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_invalid_page_param() {
        assertRestSteps.assert_response_error_detail_exists(
            ErrorDetailDto.builder()
                .name("page")
                .message("invalid value").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_negative_page_param() {
        assertRestSteps.assert_response_error_detail_exists(
            ErrorDetailDto.builder()
                .name("page")
                .message("must be greater than or equal to 0").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_invalid_page_size_param() {
        assertRestSteps.assert_response_error_detail_exists(
            ErrorDetailDto.builder()
                .name("pageSize")
                .message("invalid value").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_too_small_page_size_param() {
        assertRestSteps.assert_response_error_detail_exists(
            ErrorDetailDto.builder()
                .name("pageSize")
                .message("must be greater than or equal to 1").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_too_large_page_size_param() {
        assertRestSteps.assert_response_error_detail_exists(
            ErrorDetailDto.builder()
                .name("pageSize")
                .message("must be less than or equal to 100").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_member_id_type_mismatch() {
        assertRestSteps.assert_response_error_detail_exists(
            ErrorDetailDto.builder()
                .name("memberId")
                .message("failed to convert path variable to required type").build()
        );
    }

    @Step
    public void should_return_at_least_one_member() {
        assertThat(then().extract().response().as(PageDto.class).getContent()).isNotEmpty();
    }

    @Step
    public void should_return_members_with_displayed_names(String... expectedDisplayedNames) {
        List<MemberPublicDto> members = then().extract().response().jsonPath()
            .getList("content", MemberPublicDto.class);
        assertThat(members).hasSize(expectedDisplayedNames.length);

        List<String> displayedNames = members.stream()
            .map(MemberPublicDto::getDisplayedName)
            .collect(Collectors.toList());

        assertThat(displayedNames).containsExactlyInAnyOrder(expectedDisplayedNames);
    }

    public Long get_created_member_id() {
        MemberPublicDto createdMember = then().extract().as(MemberPublicDto.class);
        return createdMember.getId();
    }

}
