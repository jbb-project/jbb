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
import org.jbb.board.impl.BoardConfig;
import org.jbb.board.impl.forum.dao.ForumCategoryRepository;
import org.jbb.board.impl.forum.dao.ForumRepository;
import org.jbb.board.impl.forum.model.ForumCategoryEntity;
import org.jbb.board.impl.forum.model.ForumEntity;
import org.jbb.lib.core.CoreConfig;
import org.jbb.lib.db.DbConfig;
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
@ContextConfiguration(classes = {BoardConfig.class, DbConfig.class, PropertiesConfig.class, MvcConfig.class,
        CoreConfig.class, CoreConfigMocks.class, CleanH2DbAfterTestsConfig.class})
@WebAppConfiguration
public class BoardServiceIT {

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private ForumCategoryRepository categoryRepository;

    @Autowired
    private BoardService boardService;

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
        boardService.addCategory(firstCategory);
        boardService.addCategory(secondCategory);

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

        firstCategory = boardService.addCategory(firstCategory);
        boardService.addCategory(secondCategory);
        boardService.addCategory(thirdCategory);

        // when
        boardService.moveCategoryToPosition(firstCategory, 3); // then: 2 3 1
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

        boardService.addCategory(firstCategory);
        boardService.addCategory(secondCategory);
        thirdCategory = boardService.addCategory(thirdCategory);

        // when
        boardService.moveCategoryToPosition(thirdCategory, 1); // then: 3 1 2

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

        category = (ForumCategoryEntity) boardService.addCategory(category);

        // when
        category.setName("new category name");
        boardService.editCategory(category);

        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).hasSize(1);
        assertThat(forumCategories.get(0).getName()).isEqualTo("new category name");
    }

    @Test
    public void shouldAddAndGetForumWithCategories() throws Exception {
        // given
        String categoryName = "test category";
        String firstForumName = "first forum";
        String secondForumName = "second forum";
        String thirdForumName = "third forum";

        ForumCategory category = boardService.addCategory(buildCategory(categoryName));

        // when
        boardService.addForum(buildForum(firstForumName, "desc1", true), category);
        boardService.addForum(buildForum(secondForumName, "desc2", false), category);
        boardService.addForum(buildForum(thirdForumName, null, false), category);

        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).hasSize(1);
        List<Forum> forums = forumCategories.get(0).getForums();
        assertThat(forums).hasSize(3);
        assertThat(forums.get(0).getName()).isEqualTo(firstForumName);
        assertThat(forums.get(1).getName()).isEqualTo(secondForumName);
        assertThat(forums.get(2).getName()).isEqualTo(thirdForumName);
    }

    @Test
    public void shouldMoveFirstForumToLastPosition() throws Exception {
        // given
        String categoryName = "test category";
        String firstForumName = "first forum";
        String secondForumName = "second forum";
        String thirdForumName = "third forum";

        ForumCategory category = boardService.addCategory(buildCategory(categoryName));
        Forum firstForum = boardService.addForum(buildForum(firstForumName, "desc1", true), category);
        boardService.addForum(buildForum(secondForumName, "desc2", false), category);
        boardService.addForum(buildForum(thirdForumName, null, false), category);

        // when
        boardService.moveForumToPosition(firstForum, 3); // then: 2 3 1
        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).hasSize(1);
        List<Forum> forums = forumCategories.get(0).getForums();
        assertThat(forums).hasSize(3);
        assertThat(forums.get(0).getName()).isEqualTo(secondForumName);
        assertThat(forums.get(1).getName()).isEqualTo(thirdForumName);
        assertThat(forums.get(2).getName()).isEqualTo(firstForumName);
    }

    @Test
    public void shouldMoveLastForumToFirstPosition() throws Exception {
        // given
        String categoryName = "test category";
        String firstForumName = "first forum";
        String secondForumName = "second forum";
        String thirdForumName = "third forum";

        ForumCategory category = boardService.addCategory(buildCategory(categoryName));
        boardService.addForum(buildForum(firstForumName, "desc1", true), category);
        boardService.addForum(buildForum(secondForumName, "desc2", false), category);
        Forum thirdForum = boardService.addForum(buildForum(thirdForumName, null, false), category);

        // when
        boardService.moveForumToPosition(thirdForum, 1); // then: 3 1 2
        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).hasSize(1);
        List<Forum> forums = forumCategories.get(0).getForums();
        assertThat(forums).hasSize(3);
        assertThat(forums.get(0).getName()).isEqualTo(thirdForumName);
        assertThat(forums.get(1).getName()).isEqualTo(firstForumName);
        assertThat(forums.get(2).getName()).isEqualTo(secondForumName);
    }

    @Test
    public void shouldEditForum() throws Exception {
        // given
        ForumCategory category = buildCategory("test category");
        category = boardService.addCategory(category);

        ForumEntity forumEntity = buildForum("name", "description", false);
        forumEntity = (ForumEntity) boardService.addForum(forumEntity, category);

        // when
        forumEntity.setName("new forum name");
        forumEntity.setDescription("new description");
        forumEntity.setLocked(true);
        boardService.editForum(forumEntity);

        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).hasSize(1);
        List<Forum> forums = forumCategories.get(0).getForums();
        assertThat(forums).hasSize(1);
        assertThat(forums.get(0).getName()).isEqualTo("new forum name");
        assertThat(forums.get(0).getDescription()).isEqualTo("new description");
        assertThat(forums.get(0).isLocked()).isTrue();
    }

    @Test
    public void shouldRemoveCategoryWithForums() throws Exception {
        // given
        ForumCategory category = buildCategory("test category");
        category = boardService.addCategory(category);
        boardService.addForum(buildForum("name", "description", false), category);

        // when
        boardService.removeCategoryAndForums(category.getId());

        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).isEmpty();
        assertThat(categoryRepository.count()).isZero();
        assertThat(forumRepository.count()).isZero();
    }

    @Test
    public void shouldRemoveForumFromCategory_andMoveAnotherForums() throws Exception {
        // given
        String categoryName = "test category";
        String firstForumName = "first forum";
        String secondForumName = "second forum";
        String thirdForumName = "third forum";

        ForumCategory category = boardService.addCategory(buildCategory(categoryName));
        boardService.addForum(buildForum(firstForumName, "desc1", true), category);
        Forum forumToRemove = boardService.addForum(buildForum(secondForumName, "desc2", false), category);
        boardService.addForum(buildForum(thirdForumName, null, false), category);

        // when
        boardService.removeForum(forumToRemove.getId());
        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).hasSize(1);
        List<Forum> forums = forumCategories.get(0).getForums();
        assertThat(forums).hasSize(2);
        assertThat(forums.get(0).getName()).isEqualTo(firstForumName);
        assertThat(forums.get(1).getName()).isEqualTo(thirdForumName);
        assertThat(forumRepository.count()).isEqualTo(2);
    }

    @Test
    public void shouldMoveForumToAnotherCategory() throws Exception {
        // given
        String firstCategoryName = "test first category";
        String secondCategoryName = "test second category";
        String forumName = "first forum";

        ForumCategory firstCategory = buildCategory(firstCategoryName);
        ForumCategory secondCategory = buildCategory(secondCategoryName);

        firstCategory = boardService.addCategory(firstCategory);
        secondCategory = boardService.addCategory(secondCategory);
        Forum forum = boardService.addForum(buildForum(forumName, "desc1", true), firstCategory);

        // when
        boardService.moveForumToAnotherCategory(forum.getId(), secondCategory.getId());
        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).hasSize(2);
        List<Forum> firstCategoryForums = forumCategories.get(0).getForums();
        assertThat(firstCategoryForums).isEmpty();
        List<Forum> secondCategoryForums = forumCategories.get(1).getForums();
        assertThat(secondCategoryForums).hasSize(1);
        assertThat(secondCategoryForums.get(0).getName()).isEqualTo(forumName);
    }

    @Test
    public void shouldRemoveCategoryAndMoveForumToAnotherCategory() throws Exception {
        // given
        String firstCategoryName = "test first category";
        String secondCategoryName = "test second category";
        String forumName = "first forum";

        ForumCategory firstCategory = buildCategory(firstCategoryName);
        ForumCategory secondCategory = buildCategory(secondCategoryName);

        firstCategory = boardService.addCategory(firstCategory);
        secondCategory = boardService.addCategory(secondCategory);
        Forum forum = boardService.addForum(buildForum(forumName, "desc1", true), firstCategory);

        // when
        boardService.removeCategoryAndMoveForums(firstCategory.getId(), secondCategory.getId());
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