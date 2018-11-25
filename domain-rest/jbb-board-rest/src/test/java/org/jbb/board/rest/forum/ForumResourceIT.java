/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.rest.forum;

import org.assertj.core.util.Sets;
import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumException;
import org.jbb.board.api.forum.ForumNotFoundException;
import org.jbb.board.api.forum.ForumService;
import org.jbb.board.rest.BaseIT;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ForumResourceIT extends BaseIT {

    @Autowired
    private ForumService forumServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(forumServiceMock);
    }

    @Test
    public void getForumById_success() {
        // given
        Forum forum = exampleForum();
        given(forumServiceMock.getForumChecked(eq(13L))).willReturn(forum);

        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // when
        MockMvcResponse response = request.when().get("/api/v1/forums/13");

        // then
        response.then().statusCode(200);
        ForumDto resultBody = response.then().extract().body().as(ForumDto.class);
        assertThat(resultBody.getId()).isEqualTo(13L);
        assertThat(resultBody.getName()).isEqualTo("Test forum");
    }

    @Test
    public void getForumById_forumNotFound() {
        // given
        given(forumServiceMock.getForumChecked(any())).willThrow(new ForumNotFoundException(13L));

        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // when
        MockMvcResponse response = request.when().get("/api/v1/forums/13");

        // then
        assertError(response, 404, ErrorInfo.FORUM_NOT_FOUND);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingForum_shouldBePossibleForAdministrators() {
        // given
        Forum forum = exampleForum();
        given(forumServiceMock.getForumChecked(any())).willReturn(forum);
        given(forumServiceMock.editForum(any())).willReturn(forum);

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(createUpdateForumDto())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/forums/13");

        // then
        response.then().statusCode(200);
        ForumDto resultBody = response.then().extract().body().as(ForumDto.class);
        assertThat(resultBody.getId()).isEqualTo(13L);
    }

    @Test
    @WithMockUser(username = "member")
    public void updatingForum_shouldBeNotPossibleForRegularMembers() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(createUpdateForumDto())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/forums/13");

        // then
        assertError(response, 403, ErrorInfo.FORBIDDEN);
    }

    @Test
    public void updatingForum_shouldBeNotPossibleForGuests() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(createUpdateForumDto())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/forums/13");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingNotExistingForum_shouldFailed() {
        // given
        given(forumServiceMock.getForumChecked(any())).willThrow(new ForumNotFoundException(13L));

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(createUpdateForumDto())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/forums/13");

        // then
        assertError(response, 404, ErrorInfo.FORUM_NOT_FOUND);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingInvalidForum_shouldFailed() {
        // given
        Forum forum = exampleForum();
        given(forumServiceMock.getForumChecked(any())).willReturn(forum);
        given(forumServiceMock.editForum(any())).willThrow(new ForumException(Sets.newHashSet()));

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(createUpdateForumDto())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/forums/13");

        // then
        assertError(response, 400, ErrorInfo.INVALID_FORUM);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingForumPosition_shouldBePossibleForAdministrators() {
        // given
        Forum forum = exampleForum();
        given(forumServiceMock.getForumChecked(any())).willReturn(forum);

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(PositionDto.builder().position(2).build())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/forums/13/position");

        // then
        response.then().statusCode(204);
        verify(forumServiceMock).moveForumToPosition(any(), eq(3));
    }

    @Test
    @WithMockUser(username = "member")
    public void updatingForumPosition_shouldNotBePossibleForRegularMembers() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(PositionDto.builder().position(2).build())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/forums/13/position");

        // then
        assertError(response, 403, ErrorInfo.FORBIDDEN);
    }

    @Test
    public void updatingForumPosition_shouldNotBePossibleForGuests() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(PositionDto.builder().position(2).build())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/forums/13/position");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingForumPosition_forNotExistingForum_shouldFailed() {
        // given
        given(forumServiceMock.getForumChecked(any())).willThrow(new ForumNotFoundException(13L));

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(PositionDto.builder().position(2).build())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/forums/13/position");

        // then
        assertError(response, 404, ErrorInfo.FORUM_NOT_FOUND);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingForumPosition_shouldFail_forNegativePosition() {
        // given
        Forum forum = exampleForum();
        given(forumServiceMock.getForumChecked(any())).willReturn(forum);

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(PositionDto.builder().position(-6).build())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/forums/13/position");

        // then
        assertError(response, 400, ErrorInfo.VALIDATION_ERROR);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void removingForum_shouldBePossibleForAdministrators() {
        // given
        Forum forum = exampleForum();
        given(forumServiceMock.getForumChecked(any())).willReturn(forum);

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().delete("/api/v1/forums/13");

        // then
        response.then().statusCode(204);
        verify(forumServiceMock).removeForum(eq(13L));
    }

    @Test
    @WithMockUser(username = "member")
    public void removingForum_shouldNotBePossibleForRegularMembers() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().delete("/api/v1/forums/13");

        // then
        assertError(response, 403, ErrorInfo.FORBIDDEN);
    }

    @Test
    public void removingForum_shouldNotBePossibleForGuests() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().delete("/api/v1/forums/13");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void removingNotExistingForum_shouldFailed() {
        // given
        given(forumServiceMock.getForumChecked(any())).willThrow(new ForumNotFoundException(13L));


        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().delete("/api/v1/forums/13");

        // then
        assertError(response, 404, ErrorInfo.FORUM_NOT_FOUND);
    }

    private Forum exampleForum() {
        Forum forum = Mockito.mock(Forum.class);
        given(forum.getId()).willReturn(13L);
        given(forum.getName()).willReturn("Test forum");
        return forum;
    }

    private CreateUpdateForumDto createUpdateForumDto() {
        return CreateUpdateForumDto.builder()
                .name("New name")
                .build();
    }

    private void assertError(MockMvcResponse response, int httpStatus, ErrorInfo errorInfo) {
        response.then().statusCode(httpStatus);
        ErrorResponse resultBody = response.then().extract().body().as(ErrorResponse.class);
        assertThat(resultBody.getCode()).isEqualTo(errorInfo.getCode());
    }

}