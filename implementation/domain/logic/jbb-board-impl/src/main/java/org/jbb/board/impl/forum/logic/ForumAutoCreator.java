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

import org.jbb.board.api.model.ForumCategory;
import org.jbb.board.api.service.BoardService;
import org.jbb.board.impl.forum.dao.ForumCategoryRepository;
import org.jbb.board.impl.forum.dao.ForumRepository;
import org.jbb.board.impl.forum.model.ForumCategoryEntity;
import org.jbb.board.impl.forum.model.ForumEntity;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.system.event.ConnectionToDatabaseEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ForumAutoCreator {
    private final ForumRepository forumRepository;
    private final ForumCategoryRepository forumCategoryRepository;
    private final BoardService boardService;

    public ForumAutoCreator(ForumRepository forumRepository, ForumCategoryRepository forumCategoryRepository,
                            BoardService boardService, JbbEventBus jbbEventBus) {
        this.forumRepository = forumRepository;
        this.forumCategoryRepository = forumCategoryRepository;
        this.boardService = boardService;
        jbbEventBus.register(this);
    }

    @Subscribe
    @Transactional
    public void buildFirstForum(ConnectionToDatabaseEvent e) {
        if (isBoardEmpty()) {
            ForumCategory forumCategory = ForumCategoryEntity.builder()
                    .name("Test forum category")
                    .build();
            forumCategory = boardService.addCategory(forumCategory);

            ForumEntity forum = ForumEntity.builder()
                    .name("Test forum")
                    .description("This is just a test forum")
                    .locked(false)
                    .build();

            boardService.addForum(forum, forumCategory);
        }
    }

    private boolean isBoardEmpty() {
        return forumCategoryRepository.count() == 0 && forumRepository.count() == 0;
    }
}
