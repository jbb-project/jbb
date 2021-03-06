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

import org.jbb.board.api.forum.BoardService;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.impl.BaseIT;
import org.jbb.board.impl.forum.dao.ForumCategoryRepository;
import org.jbb.board.impl.forum.model.ForumCategoryEntity;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardServiceIT extends BaseIT {

    @Autowired
    private ForumCategoryRepository categoryRepository;

    @Autowired
    private BoardService boardService;

    @Before
    public void setUp() throws Exception {
        categoryRepository.deleteAll();
    }

    @Test
    public void shouldAddAndGetCategories() throws Exception {
        // given
        String firstCategoryName = "test first category";
        String secondCategoryName = "test second category";

        ForumCategoryEntity firstCategory = buildCategory(firstCategoryName, 1);
        ForumCategoryEntity secondCategory = buildCategory(secondCategoryName, 2);

        categoryRepository.save(firstCategory);
        categoryRepository.save(secondCategory);

        // when
        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).hasSize(2);
        assertThat(forumCategories.get(0).getName()).isEqualTo(firstCategoryName);
        assertThat(forumCategories.get(1).getName()).isEqualTo(secondCategoryName);
    }

    private ForumCategoryEntity buildCategory(String name, int position) {
        return ForumCategoryEntity.builder()
                .name(name)
                .position(position)
                .build();
    }

}