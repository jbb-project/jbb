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

import java.util.List;
import java.util.stream.Collectors;
import javax.cache.annotation.CacheResult;
import org.hibernate.Hibernate;
import org.jbb.board.api.forum.BoardService;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.impl.forum.dao.ForumCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardServiceImpl implements BoardService {
    private final ForumCategoryRepository categoryRepository;

    @Autowired
    public BoardServiceImpl(ForumCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    @CacheResult(cacheName = ForumCaches.BOARD_STRUCTURE)
    public List<ForumCategory> getForumCategories() {
        List<ForumCategory> categories = categoryRepository.findAllByOrderByPositionAsc().stream()
                .map(entity -> (ForumCategory) Hibernate.unproxy(entity))
                .collect(Collectors.toList());

        categories.forEach(ForumCategory::getForums);

        return categories;
    }

}
