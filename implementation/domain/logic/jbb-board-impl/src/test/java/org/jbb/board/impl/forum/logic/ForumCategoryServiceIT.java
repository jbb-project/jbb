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
import org.jbb.board.api.service.ForumCategoryService;
import org.jbb.board.api.service.ForumService;
import org.jbb.board.impl.BoardConfig;
import org.jbb.board.impl.forum.dao.ForumCategoryRepository;
import org.jbb.board.impl.forum.dao.ForumRepository;
import org.jbb.board.impl.forum.model.ForumCategoryEntity;
import org.jbb.board.impl.forum.model.ForumEntity;
import org.jbb.lib.core.CoreConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CleanH2DbAfterTestsConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BoardConfig.class, DbConfig.class, PropertiesConfig.class, MvcConfig.class, EventBusConfig.class,
        CoreConfig.class, CoreConfigMocks.class, CleanH2DbAfterTestsConfig.class})
@WebAppConfiguration
public class ForumCategoryServiceIT {

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private ForumCategoryRepository categoryRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private ForumService forumService;

    @Autowired
    private ForumCategoryService forumCategoryService;

    @Before
    public void setUp() throws Exception {
        forumRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void shouldAddAndGetCategories() throws Exception {
        // given
        String firstCategoryName = "test first category";
        String secondCategoryName = "test second category";

        ForumCategory firstCategory = buildCategory(firstCategoryName);
        ForumCategory secondCategory = buildCategory(secondCategoryName);

        // when
        forumCategoryService.addCategory(firstCategory);
        forumCategoryService.addCategory(secondCategory);

        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).hasSize(2);
        assertThat(forumCategories.get(0).getName()).isEqualTo(firstCategoryName);
        assertThat(forumCategories.get(1).getName()).isEqualTo(secondCategoryName);
    }

    @Test
    public void shouldMoveFirstCategoryToLastPosition() throws Exception {
        // given
        String firstCategoryName = "test first category";
        String secondCategoryName = "test second category";
        String thirdCategoryName = "test third category";

        ForumCategory firstCategory = buildCategory(firstCategoryName);
        ForumCategory secondCategory = buildCategory(secondCategoryName);
        ForumCategory thirdCategory = buildCategory(thirdCategoryName);

        firstCategory = forumCategoryService.addCategory(firstCategory);
        forumCategoryService.addCategory(secondCategory);
        forumCategoryService.addCategory(thirdCategory);

        // when
        forumCategoryService.moveCategoryToPosition(firstCategory, 3); // then: 2 3 1
        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).hasSize(3);
        assertThat(forumCategories.get(0).getName()).isEqualTo(secondCategoryName);
        assertThat(forumCategories.get(1).getName()).isEqualTo(thirdCategoryName);
        assertThat(forumCategories.get(2).getName()).isEqualTo(firstCategoryName);
    }

    @Test
    public void shouldMoveLastCategoryToFirstPosition() throws Exception {
        // given
        String firstCategoryName = "test first category";
        String secondCategoryName = "test second category";
        String thirdCategoryName = "test third category";

        ForumCategory firstCategory = buildCategory(firstCategoryName);
        ForumCategory secondCategory = buildCategory(secondCategoryName);
        ForumCategory thirdCategory = buildCategory(thirdCategoryName);

        forumCategoryService.addCategory(firstCategory);
        forumCategoryService.addCategory(secondCategory);
        thirdCategory = forumCategoryService.addCategory(thirdCategory);

        // when
        forumCategoryService.moveCategoryToPosition(thirdCategory, 1); // then: 3 1 2

        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).hasSize(3);
        assertThat(forumCategories.get(0).getName()).isEqualTo(thirdCategoryName);
        assertThat(forumCategories.get(1).getName()).isEqualTo(firstCategoryName);
        assertThat(forumCategories.get(2).getName()).isEqualTo(secondCategoryName);
    }

    @Test
    public void shouldEditCategoryName() throws Exception {
        // given
        ForumCategoryEntity category = buildCategory("test category");

        category = (ForumCategoryEntity) forumCategoryService.addCategory(category);

        // when
        category.setName("new category name");
        forumCategoryService.editCategory(category);

        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).hasSize(1);
        assertThat(forumCategories.get(0).getName()).isEqualTo("new category name");
    }

    @Test
    public void shouldRemoveCategoryWithForums() throws Exception {
        // given
        ForumCategory category = buildCategory("test category");
        category = forumCategoryService.addCategory(category);
        forumService.addForum(buildForum("name", "description", false), category);

        // when
        forumCategoryService.removeCategoryAndForums(category.getId());

        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).isEmpty();
        assertThat(categoryRepository.count()).isZero();
        assertThat(forumRepository.count()).isZero();
    }

    @Test
    public void shouldRemoveCategoryAndMoveForumToAnotherCategory() throws Exception {
        // given
        String firstCategoryName = "test first category";
        String secondCategoryName = "test second category";
        String forumName = "first forum";

        ForumCategory firstCategory = buildCategory(firstCategoryName);
        ForumCategory secondCategory = buildCategory(secondCategoryName);

        firstCategory = forumCategoryService.addCategory(firstCategory);
        secondCategory = forumCategoryService.addCategory(secondCategory);
        forumService.addForum(buildForum(forumName, "desc1", true), firstCategory);

        // when
        forumCategoryService.removeCategoryAndMoveForums(firstCategory.getId(), secondCategory.getId());
        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).hasSize(1);
        assertThat(forumCategories.get(0).getName()).isEqualTo(secondCategoryName);
        List<Forum> firstCategoryForums = forumCategories.get(0).getForums();
        assertThat(firstCategoryForums).hasSize(1);
        assertThat(firstCategoryForums.get(0).getName()).isEqualTo(forumName);
    }

    private ForumCategoryEntity buildCategory(String name) {
        return ForumCategoryEntity.builder()
                .name(name)
                .build();
    }

    private ForumEntity buildForum(String name, String description, boolean locked) {
        return ForumEntity.builder()
                .name(name)
                .description(description)
                .locked(locked)
                .build();
    }
}