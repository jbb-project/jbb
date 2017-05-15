/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.impl.forum.logic;

import org.jbb.board.api.model.Forum;
import org.jbb.board.api.model.ForumCategory;
import org.jbb.board.api.service.ForumCategoryService;
import org.jbb.board.api.service.ForumService;
import org.jbb.board.impl.forum.dao.ForumCategoryRepository;
import org.jbb.board.impl.forum.dao.ForumRepository;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.system.event.ConnectionToDatabaseEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ForumAutoCreatorTest {
    @Mock
    private ForumRepository forumRepositoryMock;

    @Mock
    private ForumCategoryRepository forumCategoryRepositoryMock;

    @Mock
    private ForumCategoryService forumCategoryServiceMock;

    @Mock
    private ForumService forumServiceMock;

    @Mock
    private JbbEventBus jbbEventBusMock;

    @InjectMocks
    private ForumAutoCreator forumAutoCreator;

    @Test
    public void shouldBuild_whenForumTablesAreEmpty() throws Exception {
        // given
        given(forumRepositoryMock.count()).willReturn(0L);
        given(forumCategoryRepositoryMock.count()).willReturn(0L);
        given(forumCategoryServiceMock.addCategory(any(ForumCategory.class))).willReturn(mock(ForumCategory.class));

        // when
        forumAutoCreator.createFirstForumAndForumCategoryIfBoardEmpty(new ConnectionToDatabaseEvent());

        // then
        verify(forumCategoryServiceMock, times(1)).addCategory(any(ForumCategory.class));
        verify(forumServiceMock, times(1)).addForum(any(Forum.class), any(ForumCategory.class));
    }

    @Test
    public void shouldNotBuild_whenForumTablesAreNotEmpty() throws Exception {
        // given
        given(forumCategoryRepositoryMock.count()).willReturn(1L);

        // when
        forumAutoCreator.createFirstForumAndForumCategoryIfBoardEmpty(new ConnectionToDatabaseEvent());

        // then
        verifyZeroInteractions(forumServiceMock, forumCategoryServiceMock);
    }

}