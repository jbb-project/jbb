/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.profile;

import com.google.common.collect.Sets;

import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.base.ProfileException;
import org.jbb.members.api.registration.RegistrationMetaData;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.members.rest.BaseIT;
import org.jbb.permissions.api.PermissionService;
import org.jbb.security.api.role.RoleService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ProfileResourcePutIT extends BaseIT {

    @Autowired
    MemberService memberServiceMock;

    @Autowired
    RegistrationService registrationServiceMock;

    @Autowired
    RoleService roleServiceMock;

    @Autowired
    PermissionService permissionServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(memberServiceMock, registrationServiceMock, roleServiceMock, permissionServiceMock);
    }

    @Test
    @WithMockUser(username = "member", roles = {})
    public void updateProfileForNotExistingMember() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any()))
                .willThrow(new MemberNotFoundException());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateProfileDto());
        MockMvcResponse response = request.when().put("/api/v1/members/200/profile");

        // then
        assertError(response, 404, ErrorInfo.MEMBER_NOT_FOUND);
    }

    @Test
    public void updateAnyProfile_shouldNotBePossibleForGuests() {
        // given
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateProfileDto());

        // when
        MockMvcResponse response = request.when().put("/api/v1/members/200/profile");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "member", roles = {})
    public void updatingNotOwnProfile_shouldNotBePossibleForRegularMembers() throws MemberNotFoundException {
        // given
        String username = "jacob";
        String email = "jacob@example.com";
        String displayedName = "Jacob";
        Long id = 200L;
        Member targetMember = getMemberMock(id, username, displayedName, email);
        Member currentMember = getMemberMock(201L, "omc", "Arthur", "a@nsn.com");
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(targetMember);
        given(memberServiceMock.getCurrentMemberChecked()).willReturn(currentMember);
        given(roleServiceMock.hasAdministratorRole(any())).willReturn(false);

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateProfileDto());
        MockMvcResponse response = request.when().put("/api/v1/members/200/profile");

        // then
        assertError(response, 403, ErrorInfo.UPDATE_NOT_OWN_PROFILE);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingNotOwnProfile_shouldBePossibleForAdministrators() throws MemberNotFoundException {
        // given
        String username = "jacob";
        String email = "jacob@example.com";
        String displayedName = "Jacob";
        Long id = 200L;
        Member targetMember = getMemberMock(id, username, displayedName, email);
        Member currentMember = getMemberMock(201L, "omc", "Arthur", "a@nsn.com");
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(targetMember);
        given(memberServiceMock.getCurrentMemberChecked()).willReturn(currentMember);
        given(roleServiceMock.hasAdministratorRole(any())).willReturn(true);
        given(registrationServiceMock.getRegistrationMetaData(eq(id))).willReturn(mock(
                RegistrationMetaData.class));

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateProfileDto());
        MockMvcResponse response = request.when().put("/api/v1/members/200/profile");

        // then
        response.then().statusCode(200);
        ProfileDto resultBody = response.then().extract().body().as(ProfileDto.class);
        assertThat(resultBody.getDisplayedName()).isEqualTo("Jacob");
    }

    @Test
    @WithMockUser(username = "member", roles = {})
    public void updatingOwnProfile_shouldBePossibleForRegularMembers() throws MemberNotFoundException {
        // given
        String username = "jacob";
        String email = "jacob@example.com";
        String displayedName = "Jacob";
        Long id = 200L;
        Member targetMember = getMemberMock(id, username, displayedName, email);
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(targetMember);
        given(memberServiceMock.getCurrentMemberChecked()).willReturn(targetMember);
        given(roleServiceMock.hasAdministratorRole(any())).willReturn(false);
        given(registrationServiceMock.getRegistrationMetaData(eq(id))).willReturn(mock(
                RegistrationMetaData.class));

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateProfileDto());
        MockMvcResponse response = request.when().put("/api/v1/members/200/profile");

        // then
        response.then().statusCode(200);
        ProfileDto resultBody = response.then().extract().body().as(ProfileDto.class);
        assertThat(resultBody.getDisplayedName()).isEqualTo("Jacob");
    }

    @Test
    @WithMockUser(username = "member", roles = {})
    public void updatingOwnProfile_whenInvalidDataProvided() throws MemberNotFoundException {
        // given
        String username = "jacob";
        String email = "jacob@example.com";
        String displayedName = "Jacob";
        Long id = 200L;
        Member targetMember = getMemberMock(id, username, displayedName, email);
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(targetMember);
        given(memberServiceMock.getCurrentMemberChecked()).willReturn(targetMember);
        given(roleServiceMock.hasAdministratorRole(any())).willReturn(false);
        Mockito.doThrow(new ProfileException(violation())).when(memberServiceMock).updateProfile(any(), any());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateProfileDto());
        MockMvcResponse response = request.when().put("/api/v1/members/200/profile");

        // then
        assertError(response, 400, ErrorInfo.UPDATE_PROFILE_FAILED);
    }

    private Set<? extends ConstraintViolation<?>> violation() {
        Path propertyPath = mock(Path.class);
        given(propertyPath.toString()).willReturn("displayedName");
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        given(violation.getPropertyPath()).willReturn(propertyPath);
        given(violation.getMessage()).willReturn("must be not blank");
        return Sets.newHashSet(violation);
    }

    private UpdateProfileDto updateProfileDto() {
        return UpdateProfileDto.builder()
                .displayedName("jBB Testing").build();
    }

    private Member getMemberMock(Long id, String username, String displayedName, String email) {
        Member member = mock(Member.class);
        given(member.getId()).willReturn(id);
        given(member.getUsername()).willReturn(Username.builder().value(username).build());
        given(member.getEmail()).willReturn(Email.builder().value(email).build());
        given(member.getDisplayedName())
                .willReturn(DisplayedName.builder().value(displayedName).build());
        return member;
    }

    private void assertError(MockMvcResponse response, int httpStatus, ErrorInfo errorInfo) {
        response.then().statusCode(httpStatus);
        ErrorResponse resultBody = response.then().extract().body().as(ErrorResponse.class);
        assertThat(resultBody.getCode()).isEqualTo(errorInfo.getCode());
    }

}