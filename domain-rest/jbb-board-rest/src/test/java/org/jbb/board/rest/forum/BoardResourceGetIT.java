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

import org.assertj.core.util.Lists;
import org.jbb.board.api.forum.BoardService;
import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.rest.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class BoardResourceGetIT extends BaseIT {

    @Autowired
    private BoardService boardServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(boardServiceMock);
    }

    @Test
    public void gettingBoard() {
        ForumCategory category = Mockito.mock(ForumCategory.class);
        given(category.getId()).willReturn(12L);
        given(category.getName()).willReturn("Test category");
        Forum forum = Mockito.mock(Forum.class);
        given(forum.getId()).willReturn(13L);
        given(forum.getName()).willReturn("Test forum");
        given(category.getForums()).willReturn(Lists.newArrayList(forum));

        List<ForumCategory> categories = Lists.newArrayList(category);
        given(boardServiceMock.getForumCategories()).willReturn(categories);

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/board");

        // then
        response.then().statusCode(200);
        BoardDto resultBody = response.then().extract().body().as(BoardDto.class);
        assertThat(resultBody.getForumCategories()).hasSize(1);
        assertThat(resultBody.getForumCategories().get(0).getName()).isEqualTo("Test category");
        assertThat(resultBody.getForumCategories().get(0).getForums()).hasSize(1);
        assertThat(resultBody.getForumCategories().get(0).getForums().get(0).getName()).isEqualTo("Test forum");
    }

}