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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.jbb.board.api.forum.BoardService;
import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.api.forum.ForumCategoryService;
import org.jbb.board.api.forum.ForumException;
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
        forumEntity.setClosed(true);
        forumService.editForum(forumEntity);

        List<ForumCategory> forumCategories = boardService.getForumCategories();

        // then
        assertThat(forumCategories).hasSize(1);
        List<Forum> forums = forumCategories.get(0).getForums();
        assertThat(forums).hasSize(1);
        assertThat(forums.get(0).getName()).isEqualTo("new forum name");
        assertThat(forums.get(0).getDescription()).isEqualTo("new description");
        assertThat(forums.get(0).isClosed()).isTrue();
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

    @Test
    public void shouldGetForum() throws Exception {
        // given
        String firstCategoryName = "test first category";
        String forumName = "first forum";

        ForumCategory firstCategory = buildCategory(firstCategoryName);

        firstCategory = forumCategoryService.addCategory(firstCategory);
        Forum forum = forumService.addForum(buildForum(forumName, "desc1", true), firstCategory);

        // when
        Forum result = forumService.getForum(forum.getId());

        // then
        assertThat(result.getId()).isEqualTo(forum.getId());
    }

    @Test(expected = ForumException.class)
    public void shouldThrowForumException_whenEmptyName_duringAdding() throws Exception {
        // given
        String categoryName = "category";
        String emptyName = StringUtils.EMPTY;

        ForumCategory category = forumCategoryService.addCategory(buildCategory(categoryName));

        // when
        forumService.addForum(buildForum(emptyName, null, true), category);

        // then
        // throws ForumException
    }

    @Test(expected = ForumException.class)
    public void shouldThrowForumException_whenWhitespaceName_duringAdding() throws Exception {
        // given
        String categoryName = "category";
        String emptyName = "                  ";

        ForumCategory category = forumCategoryService.addCategory(buildCategory(categoryName));

        // when
        forumService.addForum(buildForum(emptyName, null, true), category);

        // then
        // throws ForumException
    }

    @Test(expected = ForumException.class)
    public void shouldThrowForumException_whenNameLengthGreaterThan255_duringAdding() throws Exception {
        // given
        String categoryName = "category";
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
            .filteredBy(CharacterPredicates.LETTERS).build();
        String tooLongName = randomStringGenerator.generate(256);

        ForumCategory category = forumCategoryService.addCategory(buildCategory(categoryName));

        // when
        forumService.addForum(buildForum(tooLongName, null, true), category);

        // then
        // throws ForumException
    }

    @Test(expected = ForumException.class)
    public void shouldThrowForumException_whenEmptyName_duringEdit() throws Exception {
        // given
        String categoryName = "category";
        String forumName = "forum name";

        ForumCategory category = forumCategoryService.addCategory(buildCategory(categoryName));
        ForumEntity forumEntity = (ForumEntity) forumService.addForum(buildForum(forumName, null, true), category);

        // when
        forumEntity.setName(StringUtils.EMPTY);
        forumService.editForum(forumEntity);

        // then
        // throws ForumException
    }

    @Test(expected = ForumException.class)
    public void shouldThrowForumException_whenWhitespaceName_duringEdit() throws Exception {
        // given
        String categoryName = "category";
        String forumName = "forum name";

        ForumCategory category = forumCategoryService.addCategory(buildCategory(categoryName));
        ForumEntity forumEntity = (ForumEntity) forumService.addForum(buildForum(forumName, null, true), category);

        // when
        forumEntity.setName("   ");
        forumService.editForum(forumEntity);

        // then
        // throws ForumException
    }

    @Test(expected = ForumException.class)
    public void shouldThrowForumException_whenNameLengthGreaterThan255_duringEdit() throws Exception {
        // given
        String categoryName = "category";
        String forumName = "forum name";

        ForumCategory category = forumCategoryService.addCategory(buildCategory(categoryName));
        ForumEntity forumEntity = (ForumEntity) forumService.addForum(buildForum(forumName, null, true), category);

        // when
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
            .filteredBy(CharacterPredicates.LETTERS).build();
        forumEntity.setName(randomStringGenerator.generate(256));
        forumService.editForum(forumEntity);

        // then
        // throws ForumException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullIdPassed_duringGetting() throws Exception {
        // when
        forumService.getForum(null);

        // then
        // throws NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullForumPassed_duringAdding() throws Exception {
        // given
        ForumCategory anyCategory = mock(ForumCategory.class);

        // when
        forumService.addForum(null, anyCategory);

        // then
        // throws NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullCategoryPassed_duringAdding() throws Exception {
        // given
        Forum anyForum = mock(Forum.class);

        // when
        forumService.addForum(anyForum, null);

        // then
        // throws NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullForumPassed_duringMovingToPosition() throws Exception {
        // given
        Integer anyPosition = 1;

        // when
        forumService.moveForumToPosition(null, anyPosition);

        // then
        // throws NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullPositionPassed_duringMovingToPosition() throws Exception {
        // given
        Forum anyForum = mock(Forum.class);

        // when
        forumService.moveForumToPosition(anyForum, null);

        // then
        // throws NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullForumIdPassed_duringMovingToCategory() throws Exception {
        // given
        Long anyCategoryId = 122L;

        // when
        forumService.moveForumToAnotherCategory(null, anyCategoryId);

        // then
        // throws NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullCategoryIdPassed_duringMovingToCategory() throws Exception {
        // given
        Long anyForumId = 122L;

        // when
        forumService.moveForumToAnotherCategory(anyForumId, null);

        // then
        // throws NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullForumIdPassed_duringEdit() throws Exception {
        // when
        forumService.editForum(null);

        // then
        // throws NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullForumIdPassed_duringRemove() throws Exception {
        // when
        forumService.removeForum(null);

        // then
        // throws NullPointerException
    }

    private ForumEntity buildForum(String name, String description, boolean closed) {
        return ForumEntity.builder()
                .name(name)
                .description(description)
                .closed(closed)
                .build();
    }

    private ForumCategoryEntity buildCategory(String name) {
        return ForumCategoryEntity.builder()
                .name(name)
                .build();
    }


}