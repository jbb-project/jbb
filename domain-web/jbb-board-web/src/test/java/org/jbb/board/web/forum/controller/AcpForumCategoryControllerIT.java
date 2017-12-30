/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.web.forum.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.jbb.board.api.forum.BoardService;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.api.forum.ForumCategoryException;
import org.jbb.board.api.forum.ForumCategoryService;
import org.jbb.board.web.BaseIT;
import org.jbb.board.web.forum.TestbedForumCategory;
import org.jbb.board.web.forum.form.ForumCategoryForm;
import org.jbb.permissions.api.PermissionService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AcpForumCategoryControllerIT extends BaseIT {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private BoardService boardServiceMock;

    @Autowired
    private ForumCategoryService forumCategoryServiceMock;

    @Autowired
    private PermissionService permissionServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        Mockito.reset(boardServiceMock, forumCategoryServiceMock);
    }

    @Test
    public void shouldUseAcpForumCategoryView_whenGET() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/acp/general/forums/category"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/forumcategory"));
    }

    @Test
    public void shouldStoreEmptyCategoryForumInModel_whenGET_withoutIdParam() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/acp/general/forums/category"));

        // then
        result.andExpect(model().attributeExists("forumCategoryForm"));
    }

    @Test
    public void shouldFillCategory_whenGET_withIdParam() throws Exception {
        // given
        Long categoryId = 585L;
        given(forumCategoryServiceMock.getCategory(eq(categoryId))).willReturn(
                Optional.of(TestbedForumCategory.builder()
                        .id(categoryId)
                        .name("category")
                        .build())
        );

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/forums/category").param("id", "585"));

        // then
        ForumCategoryForm form = (ForumCategoryForm) result.andReturn().getModelAndView().getModel().get("forumCategoryForm");

        assertThat(form.getId()).isEqualTo(categoryId);
        assertThat(form.getName()).isEqualTo("category");
    }

    @Test
    public void shouldInvokeAddNewCategory_whenPOST_withoutId() throws Exception {
        // given
        given(permissionServiceMock.checkPermission(any())).willReturn(true);


        // when
        ResultActions result = mockMvc.perform(
                post("/acp/general/forums/category")
                        .param("name", "category name")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acp/general/forums"));

        verify(forumCategoryServiceMock, times(1)).addCategory(any(ForumCategory.class));
    }

    @Test
    public void shouldInvokeEditCategory_whenPOST_withId() throws Exception {
        // given
        given(forumCategoryServiceMock.getCategory(any())).willReturn(
                Optional.of(TestbedForumCategory.builder().forums(Lists.newArrayList()).build())
        );
        given(permissionServiceMock.checkPermission(any())).willReturn(true);

        // when
        ResultActions result = mockMvc.perform(
                post("/acp/general/forums/category")
                        .param("id", "3333")
                        .param("name", "category name")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acp/general/forums"));

        verify(forumCategoryServiceMock, times(1)).editCategory(any(ForumCategory.class));
    }

    @Test
    public void UseAcpForumCategoryView_whenPOST_andForumCategoryException() throws Exception {
        // given
        given(forumCategoryServiceMock.addCategory(any())).willThrow(new ForumCategoryException(Sets.newHashSet()));
        given(permissionServiceMock.checkPermission(any())).willReturn(true);

        // when
        ResultActions result = mockMvc.perform(
                post("/acp/general/forums/category")
                        .param("name", "  ")
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/forumcategory"));
    }

    @Test
    public void shouldInvokeMoveCategory_whenMoveUpPOST() throws Exception {
        // given
        given(forumCategoryServiceMock.getCategory(any())).willReturn(
                Optional.of(TestbedForumCategory.builder().forums(Lists.newArrayList()).build())
        );

        // when
        ResultActions result = mockMvc.perform(
                post("/acp/general/forums/category/moveup")
                        .param("id", "3333")
                        .param("position", "4")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acp/general/forums"));

        verify(forumCategoryServiceMock, times(1)).moveCategoryToPosition(any(), eq(3));
    }

    @Test
    public void shouldInvokeMoveCategory_whenMoveDownPOST() throws Exception {
        // given
        given(forumCategoryServiceMock.getCategory(any())).willReturn(
                Optional.of(TestbedForumCategory.builder().forums(Lists.newArrayList()).build())
        );

        // when
        ResultActions result = mockMvc.perform(
                post("/acp/general/forums/category/movedown")
                        .param("id", "3333")
                        .param("position", "4")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acp/general/forums"));

        verify(forumCategoryServiceMock, times(1)).moveCategoryToPosition(any(), eq(5));
    }

    @Test
    public void shouldUseAcpForumCategoryDeleteView_whenDeletePOST() throws Exception {
        // given
        given(forumCategoryServiceMock.getCategory(eq(1234L))).willReturn(
                Optional.of(TestbedForumCategory.builder()
                        .name("category to remove")
                        .id(1234L)
                        .build())
        );

        given(boardServiceMock.getForumCategories()).willReturn(Lists.newArrayList(
                TestbedForumCategory.builder()
                        .name("category1")
                        .id(1L).build()
        ));

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/forums/category/delete")
                .param("id", "1234")
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/forumcategory-delete"))
                .andExpect(model().attribute("forumCategoryName", "category to remove"))
                .andExpect(model().attributeExists("availableCategories", "forumCategoryDeleteForm"));
    }

    @Test
    public void shouldInvokeRemovingWithForums_whenDeleteConfirmedPOST_andFlagTrue() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/acp/general/forums/category/delete/confirmed")
                .param("id", "1234")
                .param("removeWithForums", "true")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acp/general/forums"));

        verify(forumCategoryServiceMock, times(1)).removeCategoryAndForums(eq(1234L));
    }

    @Test
    public void shouldInvokeRemovingWithMovingForums_whenDeleteConfirmedPOST_andFlagFalse() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/acp/general/forums/category/delete/confirmed")
                .param("id", "1234")
                .param("removeWithForums", "false")
                .param("newCategoryId", "500")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acp/general/forums"));

        verify(forumCategoryServiceMock, times(1)).removeCategoryAndMoveForums(1234L, 500L);
    }
}