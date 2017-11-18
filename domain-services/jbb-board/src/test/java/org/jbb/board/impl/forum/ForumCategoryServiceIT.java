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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.jbb.board.api.forum.BoardService;
import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.api.forum.ForumCategoryException;
import org.jbb.board.api.forum.ForumCategoryService;
import org.jbb.board.api.forum.ForumService;
import org.jbb.board.impl.BoardConfig;
import org.jbb.board.impl.forum.dao.ForumCategoryRepository;
import org.jbb.board.impl.forum.dao.ForumRepository;
import org.jbb.board.impl.forum.model.ForumCategoryEntity;
import org.jbb.board.impl.forum.model.ForumEntity;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BoardConfig.class, DbConfig.class, PropertiesConfig.class, MvcConfig.class, EventBusConfig.class,
    CommonsConfig.class, MockCommonsConfig.class})
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

    @Test
    public void shouldGetCategory() throws Exception {
        // given
        ForumCategory category = buildCategory("category");
        ForumCategoryEntity forumCategoryEntity = (ForumCategoryEntity) forumCategoryService.addCategory(category);

        // when
        Optional<ForumCategory> result = forumCategoryService.getCategory(forumCategoryEntity.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(forumCategoryEntity.getId());
    }

    @Test
    public void shouldGetCategoryWithForum() throws Exception {
        // given
        ForumCategory category = buildCategory("category");
        ForumCategoryEntity forumCategoryEntity = (ForumCategoryEntity) forumCategoryService.addCategory(category);

        ForumEntity forumEntity = (ForumEntity) forumService.addForum(buildForum("name", "description", false), forumCategoryEntity);

        // when
        ForumCategory result = forumCategoryService.getCategoryWithForum(forumEntity);

        // then
        assertThat(result.getId()).isEqualTo(forumCategoryEntity.getId());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullCategoryPassed_duringAddition() throws Exception {
        // when
        forumCategoryService.addCategory(null);

        // then
        // throws NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullCategoryPassed_duringMoving() throws Exception {
        // given
        Integer anyPosition = 4;

        // when
        forumCategoryService.moveCategoryToPosition(null, anyPosition);

        // then
        // throws NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullPositionPassed_duringMoving() throws Exception {
        // given
        ForumCategory forumCategory = mock(ForumCategory.class);

        // when
        forumCategoryService.moveCategoryToPosition(forumCategory, null);

        // then
        // throws NullPointerException
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenInvalidPositionPassed_duringMoving() throws Exception {
        // given
        ForumCategory firstCategory = buildCategory("firstCategory");
        ForumCategory secondCategory = buildCategory("secondCategory");
        ForumCategoryEntity forumCategoryEntity = (ForumCategoryEntity) forumCategoryService.addCategory(firstCategory);
        forumCategoryService.addCategory(secondCategory);

        // when
        forumCategoryService.moveCategoryToPosition(forumCategoryEntity, 3);

        // then
        // throws IllegalArgumentException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullCategoryPassed_duringEdition() throws Exception {
        // when
        forumCategoryService.editCategory(null);

        // then
        // throws NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullIdPassed_duringGetting() throws Exception {
        // when
        forumCategoryService.getCategory(null);

        // then
        // throws NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullIdPassed_duringGettingWithForums() throws Exception {
        // when
        forumCategoryService.getCategoryWithForum(null);

        // then
        // throws NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullIdPassed_duringDeletionWithForums() throws Exception {
        // when
        forumCategoryService.removeCategoryAndForums(null);

        // then
        // throws NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullCategoryIdPassed_duringDeletionWithMovingForums() throws Exception {
        // given
        Long anyNewCategoryId = 12L;

        // when
        forumCategoryService.removeCategoryAndMoveForums(null, anyNewCategoryId);

        // then
        // throws NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullNewCategoryIdPassed_duringDeletionWithMovingForums() throws Exception {
        // given
        Long anyCategoryId = 12L;

        // when
        forumCategoryService.removeCategoryAndMoveForums(anyCategoryId, null);

        // then
        // throws NullPointerException
    }

    @Test(expected = ForumCategoryException.class)
    public void shouldThrowForumCategoryException_whenEmptyName_duringAddition() throws Exception {
        // given
        String emptyName = StringUtils.EMPTY;

        // when
        forumCategoryService.addCategory(buildCategory(emptyName));

        // then
        // throw ForumCategoryException
    }

    @Test(expected = ForumCategoryException.class)
    public void shouldThrowForumCategoryException_whenWhitespaceName_duringAddition() throws Exception {
        // given
        String emptyName = "            ";

        // when
        forumCategoryService.addCategory(buildCategory(emptyName));

        // then
        // throw ForumCategoryException
    }

    @Test(expected = ForumCategoryException.class)
    public void shouldThrowForumCategoryException_whenNameLengthGreaterThan255_duringAddition() throws Exception {
        // given
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
            .filteredBy(CharacterPredicates.LETTERS).build();
        String tooLongName = randomStringGenerator.generate(256);

        // when
        forumCategoryService.addCategory(buildCategory(tooLongName));

        // then
        // throw ForumCategoryException
    }

    @Test(expected = ForumCategoryException.class)
    public void shouldThrowForumCategoryException_whenEmptyName_duringEdit() throws Exception {
        // given
        ForumCategory firstCategory = buildCategory("firstCategory");
        ForumCategoryEntity forumCategoryEntity = (ForumCategoryEntity) forumCategoryService.addCategory(firstCategory);
        forumCategoryService.addCategory(forumCategoryEntity);

        String emptyName = StringUtils.EMPTY;

        // when
        forumCategoryEntity.setName(emptyName);
        forumCategoryService.editCategory(forumCategoryEntity);

        // then
        // throw ForumCategoryException
    }

    @Test(expected = ForumCategoryException.class)
    public void shouldThrowForumCategoryException_whenWhitespaceName_duringEdit() throws Exception {
        // given
        ForumCategory firstCategory = buildCategory("firstCategory");
        ForumCategoryEntity forumCategoryEntity = (ForumCategoryEntity) forumCategoryService.addCategory(firstCategory);
        forumCategoryService.addCategory(forumCategoryEntity);

        String emptyName = "                         ";

        // when
        forumCategoryEntity.setName(emptyName);
        forumCategoryService.editCategory(forumCategoryEntity);

        // then
        // throw ForumCategoryException
    }

    @Test(expected = ForumCategoryException.class)
    public void shouldThrowForumCategoryException_whenNameLengthGreaterThan255_duringEdit() throws Exception {
        // given
        ForumCategory firstCategory = buildCategory("firstCategory");
        ForumCategoryEntity forumCategoryEntity = (ForumCategoryEntity) forumCategoryService.addCategory(firstCategory);
        forumCategoryService.addCategory(forumCategoryEntity);

        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
            .filteredBy(CharacterPredicates.LETTERS).build();
        String tooLongName = randomStringGenerator.generate(256);

        // when
        forumCategoryEntity.setName(tooLongName);
        forumCategoryService.editCategory(forumCategoryEntity);

        // then
        // throw ForumCategoryException
    }

    private ForumCategoryEntity buildCategory(String name) {
        return ForumCategoryEntity.builder()
                .name(name)
                .build();
    }

    private ForumEntity buildForum(String name, String description, boolean closed) {
        return ForumEntity.builder()
                .name(name)
                .description(description)
                .closed(closed)
                .build();
    }
}