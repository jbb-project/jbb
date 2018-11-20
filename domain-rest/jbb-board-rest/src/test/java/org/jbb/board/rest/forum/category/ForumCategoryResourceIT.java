/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.rest.forum.category;

import org.assertj.core.util.Lists;
import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.api.forum.ForumCategoryNotFoundException;
import org.jbb.board.api.forum.ForumCategoryService;
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

public class ForumCategoryResourceIT extends BaseIT {

    @Autowired
    private ForumCategoryService forumCategoryServiceMock;

    @Autowired
    private ForumService forumServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(forumCategoryServiceMock);
        Mockito.reset(forumServiceMock);
    }

    @Test
    public void getForumCategoryById_success() {
        // given
        ForumCategory forumCategory = exampleForumCategory();
        given(forumCategoryServiceMock.getCategoryChecked(eq(20L))).willReturn(forumCategory);

        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // when
        MockMvcResponse response = request.when().get("/api/v1/forum-categories/20");

        // then
        response.then().statusCode(200);
        ForumCategoryDto resultBody = response.then().extract().body().as(ForumCategoryDto.class);
        assertThat(resultBody.getId()).isEqualTo(20L);
        assertThat(resultBody.getName()).isEqualTo("Category");
    }

    @Test
    public void getForumCategoryById_forumCategoryNotFound() {
        // given
        given(forumCategoryServiceMock.getCategoryChecked(any())).willThrow(new ForumCategoryNotFoundException(20L));

        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // when
        MockMvcResponse response = request.when().get("/api/v1/forum-categories/20");

        // then
        assertError(response, 404, ErrorInfo.FORUM_CATEGORY_NOT_FOUND);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void creatingForumCategory_shouldBePossibleForAdministrators() {
        // given
        ForumCategory forumCategory = exampleForumCategory();
        given(forumCategoryServiceMock.addCategory(any())).willReturn(forumCategory);

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(createUpdateForumCategoryDto())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().post("/api/v1/forum-categories");

        // then
        response.then().statusCode(201);
        ForumCategoryDto resultBody = response.then().extract().body().as(ForumCategoryDto.class);
        assertThat(resultBody.getId()).isEqualTo(20L);
        assertThat(resultBody.getName()).isEqualTo("Category");
    }

    @Test
    @WithMockUser(username = "member")
    public void creatingForumCategory_shouldBeNotPossibleForRegularMembers() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(createUpdateForumCategoryDto())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().post("/api/v1/forum-categories");

        // then
        assertError(response, 403, ErrorInfo.FORBIDDEN);
    }

    @Test
    public void creatingForumCategory_shouldBeNotPossibleForGuests() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(createUpdateForumCategoryDto())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().post("/api/v1/forum-categories");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    private Forum exampleForum() {
        Forum forum = Mockito.mock(Forum.class);
        given(forum.getId()).willReturn(77L);
        given(forum.getName()).willReturn("New forum");
        return forum;
    }

    private ForumCategory exampleForumCategory() {
        Forum forum = Mockito.mock(Forum.class);
        given(forum.getId()).willReturn(22L);
        given(forum.getName()).willReturn("Test forum");

        ForumCategory forumCategory = Mockito.mock(ForumCategory.class);
        given(forumCategory.getId()).willReturn(20L);
        given(forumCategory.getName()).willReturn("Category");
        given(forumCategory.getForums()).willReturn(Lists.newArrayList(forum));
        return forumCategory;
    }

    private void assertError(MockMvcResponse response, int httpStatus, ErrorInfo errorInfo) {
        response.then().statusCode(httpStatus);
        ErrorResponse resultBody = response.then().extract().body().as(ErrorResponse.class);
        assertThat(resultBody.getCode()).isEqualTo(errorInfo.getCode());
    }

    private CreateUpdateForumCategoryDto createUpdateForumCategoryDto() {
        return CreateUpdateForumCategoryDto.builder()
                .name("Category").build();
    }

}