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

import com.github.zafarkhaja.semver.Version;
import lombok.RequiredArgsConstructor;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.api.forum.ForumCategoryService;
import org.jbb.board.api.forum.ForumService;
import org.jbb.board.impl.forum.dao.ForumRepository;
import org.jbb.board.impl.forum.model.ForumCategoryEntity;
import org.jbb.board.impl.forum.model.ForumEntity;
import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ForumInstallAction implements InstallUpdateAction {

    private final ForumRepository forumRepository;
    private final ForumCategoryService forumCategoryService;
    private final ForumService forumService;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_8_0;
    }

    @Override
    public void install(InstallationData installationData) {
        if (isBoardNotEmpty()) {
            return;
        }

        ForumCategory forumCategory = ForumCategoryEntity.builder()
            .name("Test forum category")
            .build();
        forumCategory = forumCategoryService.addCategory(forumCategory);

        ForumEntity forum = ForumEntity.builder()
            .name("Test forum")
            .description("This is just a test forum")
            .closed(false)
            .build();

        forumService.addForum(forum, forumCategory);
    }

    private boolean isBoardNotEmpty() {
        return forumRepository.count() > 0;
    }
}
