/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.impl.forum;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.api.forum.ForumCategoryService;
import org.jbb.board.api.forum.ForumService;
import org.jbb.board.impl.forum.dao.ForumRepository;
import org.jbb.install.InstallationData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ForumInstallActionTest {

    @Mock
    private ForumRepository forumRepositoryMock;

    @Mock
    private ForumCategoryService forumCategoryServiceMock;

    @Mock
    private ForumService forumServiceMock;

    @InjectMocks
    private ForumInstallAction forumInstallAction;

    @Test
    public void shouldBuild_whenNoForum() throws Exception {
        // given
        given(forumRepositoryMock.count()).willReturn(0L);
        given(forumCategoryServiceMock.addCategory(any(ForumCategory.class)))
            .willReturn(mock(ForumCategory.class));

        // when
        forumInstallAction.install(mock(InstallationData.class));

        // then
        verify(forumCategoryServiceMock, times(1)).addCategory(any(ForumCategory.class));
        verify(forumServiceMock, times(1)).addForum(any(Forum.class), any(ForumCategory.class));
    }

    @Test
    public void shouldNotBuild_whenAnyForumExists() throws Exception {
        // given
        given(forumRepositoryMock.count()).willReturn(1L);

        // when
        forumInstallAction.install(mock(InstallationData.class));

        // then
        verifyZeroInteractions(forumServiceMock, forumCategoryServiceMock);
    }

}