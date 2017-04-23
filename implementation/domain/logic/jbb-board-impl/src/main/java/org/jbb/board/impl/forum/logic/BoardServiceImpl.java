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
import org.jbb.board.api.service.BoardService;
import org.jbb.board.impl.forum.dao.ForumCategoryRepository;
import org.jbb.board.impl.forum.dao.ForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    private final ForumRepository forumRepository;
    private final ForumCategoryRepository categoryRepository;

    @Autowired
    public BoardServiceImpl(ForumRepository forumRepository, ForumCategoryRepository categoryRepository) {
        this.forumRepository = forumRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ForumCategory> getForumCategories() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ForumCategory addCategory(ForumCategory forumCategory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ForumCategory moveCategoryToPosition(ForumCategory forumCategory, Integer position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ForumCategory editCategory(ForumCategory forumCategory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeCategoryAndForums(Long categoryId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeCategoryAndMoveForums(Long categoryId, Long newCategoryId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Forum addForum(Forum forum, ForumCategory category) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Forum moveForumToPosition(Forum forum, Integer position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Forum moveForumToAnotherCategory(Long forumId, Long categoryId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Forum editForum(Forum forum) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeForum(Long forumId) {
        throw new UnsupportedOperationException();
    }
}
