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
public class ForumServiceIT {

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
    public void shouldAddAndGetForumWithCategories() throws Exception {
        // given
        String categoryName = "test category";
        String firstForumName = "first forum";
        String secondForumName = "second forum";
        String thirdForumName = "third forum";

        ForumCategory category = forumCategoryService.addCategory(buildCategory(categoryName));

        // when
        forumService.addForum(buildForum(firstForumName, "desc1", true), category);
        forumService.addForum(buildForum(secondForumName, "desc2", false), category);
        forumService.addForum(buildForum(thirdForumName, null, false), category);

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

        ForumCategory category = forumCategoryService.addCategory(buildCategory(categoryName));
        Forum firstForum = forumService.addForum(buildForum(firstForumName, "desc1", true), category);
        forumService.addForum(buildForum(secondForumName, "desc2", false), category);
        forumService.addForum(buildForum(thirdForumName, null, false), category);

        // when
        forumService.moveForumToPosition(firstForum, 3); // then: 2 3 1
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

        ForumCategory category = forumCategoryService.addCategory(buildCategory(categoryName));
        forumService.addForum(buildForum(firstForumName, "desc1", true), category);
        forumService.addForum(buildForum(secondForumName, "desc2", false), category);
        Forum thirdForum = forumService.addForum(buildForum(thirdForumName, null, false), category);

        // when
        forumService.moveForumToPosition(thirdForum, 1); // then: 3 1 2
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
        category = forumCategoryService.addCategory(category);

        ForumEntity forumEntity = buildForum("name", "description", false);
        forumEntity = (ForumEntity) forumService.addForum(forumEntity, category);

        // when
        forumEntity.setName("new forum name");
        forumEntity.setDescription("new description");
        forumEntity.setLocked(true);
        forumService.editForum(forumEntity);

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
    public void shouldRemoveForumFromCategory_andMoveAnotherForums() throws Exception {
        // given
        String categoryName = "test category";
        String firstForumName = "first forum";
        String secondForumName = "second forum";
        String thirdForumName = "third forum";

        ForumCategory category = forumCategoryService.addCategory(buildCategory(categoryName));
        forumService.addForum(buildForum(firstForumName, "desc1", true), category);
        Forum forumToRemove = forumService.addForum(buildForum(secondForumName, "desc2", false), category);
        forumService.addForum(buildForum(thirdForumName, null, false), category);

        // when
        forumService.removeForum(forumToRemove.getId());
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

        firstCategory = forumCategoryService.addCategory(firstCategory);
        secondCategory = forumCategoryService.addCategory(secondCategory);
        Forum forum = forumService.addForum(buildForum(forumName, "desc1", true), firstCategory);

        // when
        forumService.moveForumToAnotherCategory(forum.getId(), secondCategory.getId());
        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).hasSize(2);
        List<Forum> firstCategoryForums = forumCategories.get(0).getForums();
        assertThat(firstCategoryForums).isEmpty();
        List<Forum> secondCategoryForums = forumCategories.get(1).getForums();
        assertThat(secondCategoryForums).hasSize(1);
        assertThat(secondCategoryForums.get(0).getName()).isEqualTo(forumName);
    }

    private ForumEntity buildForum(String name, String description, boolean locked) {
        return ForumEntity.builder()
                .name(name)
                .description(description)
                .locked(locked)
                .build();
    }

    private ForumCategoryEntity buildCategory(String name) {
        return ForumCategoryEntity.builder()
                .name(name)
                .build();
    }


}