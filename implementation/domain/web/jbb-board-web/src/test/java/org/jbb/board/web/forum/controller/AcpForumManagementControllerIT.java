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

import org.jbb.board.api.model.Forum;
import org.jbb.board.api.model.ForumCategory;
import org.jbb.board.api.service.BoardService;
import org.jbb.board.web.BoardWebConfig;
import org.jbb.board.web.base.BoardConfigMock;
import org.jbb.lib.core.CoreConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCoreConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CoreConfig.class, MvcConfig.class, BoardWebConfig.class, PropertiesConfig.class,
        BoardConfigMock.class, MockCoreConfig.class})
public class AcpForumManagementControllerIT {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private BoardService boardServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldUseAcpForumManagementView_whenAcpForumManagementUrlInvoked() throws Exception {
        // given
        given(boardServiceMock.getForumCategories()).willReturn(Lists.newArrayList());

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/forums"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/forums"));
    }

    @Test
    public void shouldStoreForumStructureInModel_whenAcpForumManagementUrlInvoked() throws Exception {
        // given
        given(boardServiceMock.getForumCategories()).willReturn(exampleForumStructure());

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/forums"));

        // then
        result.andExpect(status().isOk())
                .andExpect(model().attributeExists("forumStructure"));
    }

    private List<ForumCategory> exampleForumStructure() {
        ForumCategory firstCategory = new ForumCategory() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getName() {
                return "example category";
            }

            @Override
            public List<Forum> getForums() {
                return Lists.newArrayList(new Forum() {
                    @Override
                    public Long getId() {
                        return 1L;
                    }

                    @Override
                    public String getName() {
                        return "forum name";
                    }

                    @Override
                    public String getDescription() {
                        return null;
                    }

                    @Override
                    public Boolean isClosed() {
                        return false;
                    }
                });
            }
        };

        return Lists.newArrayList(firstCategory);
    }

}