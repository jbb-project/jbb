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

import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.api.forum.ForumCategoryService;
import org.jbb.board.api.forum.ForumService;
import org.jbb.board.impl.forum.install.ForumInstallAction;
import org.jbb.install.InstallationData;
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

@RunWith(MockitoJUnitRunner.class)
public class ForumInstallActionTest {

    @Mock
    private ForumCategoryService forumCategoryServiceMock;

    @Mock
    private ForumService forumServiceMock;

    @InjectMocks
    private ForumInstallAction forumInstallAction;

    @Test
    public void shouldBuild_whenInstallInvoked() throws Exception {
        // given
        given(forumCategoryServiceMock.addCategory(any(ForumCategory.class)))
                .willReturn(mock(ForumCategory.class));

        // when
        forumInstallAction.install(mock(InstallationData.class));

        // then
        verify(forumCategoryServiceMock, times(1)).addCategory(any(ForumCategory.class));
        verify(forumServiceMock, times(1)).addForum(any(Forum.class), any(ForumCategory.class));
    }

}