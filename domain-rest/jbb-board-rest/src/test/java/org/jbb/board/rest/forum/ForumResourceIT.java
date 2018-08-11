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

import org.jbb.board.api.forum.Forum;
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

    private Forum exampleForum() {
        Forum forum = Mockito.mock(Forum.class);
        given(forum.getId()).willReturn(13L);
        given(forum.getName()).willReturn("Test forum");
        return forum;
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