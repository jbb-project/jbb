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

import com.google.common.eventbus.Subscribe;

import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.api.forum.ForumCategoryService;
import org.jbb.board.api.forum.ForumService;
import org.jbb.board.impl.forum.dao.ForumCategoryRepository;
import org.jbb.board.impl.forum.dao.ForumRepository;
import org.jbb.board.impl.forum.model.ForumCategoryEntity;
import org.jbb.board.impl.forum.model.ForumEntity;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.system.event.ConnectionToDatabaseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ForumAutoCreator {
    private final ForumRepository forumRepository;
    private final ForumCategoryRepository forumCategoryRepository;
    private final ForumCategoryService forumCategoryService;
    private final ForumService forumService;

    @Autowired
    public ForumAutoCreator(ForumRepository forumRepository, ForumCategoryRepository forumCategoryRepository,
                            ForumCategoryService forumCategoryService, ForumService forumService, JbbEventBus jbbEventBus) {
        this.forumRepository = forumRepository;
        this.forumCategoryRepository = forumCategoryRepository;
        this.forumCategoryService = forumCategoryService;
        this.forumService = forumService;
        jbbEventBus.register(this);
    }


    @Subscribe
    @Transactional
    public void createFirstForumAndForumCategoryIfBoardEmpty(ConnectionToDatabaseEvent e) {
        if (isBoardEmpty()) {
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
    }

    private boolean isBoardEmpty() {
        return forumCategoryRepository.count() == 0 && forumRepository.count() == 0;
    }
}
