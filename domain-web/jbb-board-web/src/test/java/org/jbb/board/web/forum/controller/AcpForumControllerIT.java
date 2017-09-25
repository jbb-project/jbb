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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Optional;
import org.jbb.board.api.forum.BoardService;
import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.api.forum.ForumCategoryService;
import org.jbb.board.api.forum.ForumException;
import org.jbb.board.api.forum.ForumService;
import org.jbb.board.web.BoardWebConfig;
import org.jbb.board.web.base.BoardConfigMock;
import org.jbb.board.web.forum.TestbedForum;
import org.jbb.board.web.forum.TestbedForumCategory;
import org.jbb.board.web.forum.form.ForumForm;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.permissions.api.PermissionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CommonsConfig.class, MvcConfig.class, BoardWebConfig.class, PropertiesConfig.class,
        BoardConfigMock.class, MockCommonsConfig.class})
public class AcpForumControllerIT {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private BoardService boardServiceMock;

    @Autowired
    private ForumCategoryService forumCategoryServiceMock;

    @Autowired
    private ForumService forumServiceMock;

    @Autowired
    private PermissionService permissionServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        Mockito.reset(boardServiceMock, forumCategoryServiceMock, forumServiceMock);
    }

    @Test
    public void shouldUseAcpForumView_whenGET() throws Exception {
        // given
        given(boardServiceMock.getForumCategories()).willReturn(Lists.newArrayList(
                TestbedForumCategory.builder()
                        .name("category1")
                        .id(1L).build()
        ));

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/forums/forum"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/forum"))
                .andExpect(model().attributeExists("availableCategories"));
    }

    @Test
    public void shouldStoreEmptyForumInModel_whenGET_withoutIdParam() throws Exception {
        // given
        given(boardServiceMock.getForumCategories()).willReturn(Lists.newArrayList(
                TestbedForumCategory.builder()
                        .name("category1")
                        .id(1L).build()
        ));

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/forums/forum"));

        // then
        result.andExpect(model().attributeExists("forumForm"));
    }

    @Test
    public void shouldFillForum_whenGET_withIdParam() throws Exception {
        // given
        Long forumId = 585L;
        given(forumServiceMock.getForum(eq(forumId))).willReturn(
                TestbedForum.builder()
                        .id(forumId)
                        .name("forum name")
                        .build()
        );

        given(forumCategoryServiceMock.getCategoryWithForum(any())).willReturn(
                TestbedForumCategory.builder()
                        .id(106L)
                        .name("category")
                        .build()
        );

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/forums/forum").param("id", "585"));

        // then
        ForumForm form = (ForumForm) result.andReturn().getModelAndView().getModel().get("forumForm");

        assertThat(form.getId()).isEqualTo(forumId);
        assertThat(form.getName()).isEqualTo("forum name");
        assertThat(form.getCategoryId()).isEqualTo(106L);
    }

    @Test
    public void shouldInvokeAddNewForum_whenPOST_withoutId() throws Exception {
        // given
        given(forumCategoryServiceMock.getCategory(any())).willReturn(Optional.of(mock(ForumCategory.class)));
        given(permissionServiceMock.checkPermission(any())).willReturn(true);

        // when
        ResultActions result = mockMvc.perform(
                post("/acp/general/forums/forum")
                        .param("name", "forum name")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acp/general/forums"));

        verify(forumServiceMock, times(1)).addForum(any(Forum.class), any());
    }

    @Test
    public void shouldInvokeEditForum_withoutCategoryChange_whenPOST_withId_andTheSameCategory() throws Exception {
        // given
        given(forumCategoryServiceMock.getCategoryWithForum(any())).willReturn(
                TestbedForumCategory.builder()
                        .id(170L)
                        .name("category")
                        .build()
        );
        given(permissionServiceMock.checkPermission(any())).willReturn(true);

        // when
        ResultActions result = mockMvc.perform(
                post("/acp/general/forums/forum")
                        .param("id", "200")
                        .param("categoryId", "170")
                        .param("name", "forum name")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acp/general/forums"));

        verify(forumServiceMock, times(1)).editForum(any(Forum.class));
        verify(forumServiceMock, times(0)).moveForumToAnotherCategory(any(), any());
    }

    @Test
    public void shouldInvokeEditForum_andCategoryChange_whenPOST_withId_andDifferentCategory() throws Exception {
        // given
        given(forumCategoryServiceMock.getCategoryWithForum(any())).willReturn(
                TestbedForumCategory.builder()
                        .id(120L)
                        .name("another category")
                        .build()
        );
        given(permissionServiceMock.checkPermission(any())).willReturn(true);

        // when
        ResultActions result = mockMvc.perform(
                post("/acp/general/forums/forum")
                        .param("id", "200")
                        .param("categoryId", "170")
                        .param("name", "forum name")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acp/general/forums"));

        verify(forumServiceMock, times(1)).editForum(any(Forum.class));
        verify(forumServiceMock, times(1)).moveForumToAnotherCategory(any(), any());
    }

    @Test
    public void UseAcpForumView_whenPOST_andForumException() throws Exception {
        // given
        given(forumCategoryServiceMock.getCategory(any())).willReturn(Optional.of(mock(ForumCategory.class)));
        given(forumServiceMock.addForum(any(), any())).willThrow(new ForumException(Sets.newHashSet()));
        given(permissionServiceMock.checkPermission(any())).willReturn(true);

        // when
        ResultActions result = mockMvc.perform(
                post("/acp/general/forums/forum")
                        .param("name", "  ")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acp/general/forums/forum"));
    }

    @Test
    public void shouldInvokeMoveForumUp_whenMoveUpPOST() throws Exception {

        // when
        ResultActions result = mockMvc.perform(
                post("/acp/general/forums/forum/moveup")
                        .param("id", "3333")
                        .param("position", "7")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acp/general/forums"));

        verify(forumServiceMock, times(1)).moveForumToPosition(any(), eq(6));
    }

    @Test
    public void shouldInvokeMoveForumDown_whenMoveDownPOST() throws Exception {

        // when
        ResultActions result = mockMvc.perform(
                post("/acp/general/forums/forum/movedown")
                        .param("id", "3333")
                        .param("position", "7")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acp/general/forums"));

        verify(forumServiceMock, times(1)).moveForumToPosition(any(), eq(8));
    }

    @Test
    public void shouldUseAcpForumDeleteView_whenDeletePOST() throws Exception {
        // given
        given(forumServiceMock.getForum(eq(122L))).willReturn(
                TestbedForum.builder()
                        .id(122L)
                        .name("forum to remove")
                        .build()
        );

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/forums/forum/delete")
                .param("id", "122")
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/forum-delete"))
                .andExpect(model().attribute("forumName", "forum to remove"));
    }

    @Test
    public void shouldInvokeRemovingForum_whenDeleteConfirmedPOST() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/acp/general/forums/forum/delete/confirmed")
                .param("id", "1234")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acp/general/forums"));

        verify(forumServiceMock, times(1)).removeForum(eq(1234L));
    }

}