/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.api.service;

import org.jbb.board.api.model.Forum;
import org.jbb.board.api.model.ForumCategory;

import java.util.List;

public interface BoardService {

    List<ForumCategory> getForumCategories();

    ForumCategory addCategory(ForumCategory forumCategory);

    ForumCategory moveCategoryToPosition(ForumCategory forumCategory, Integer position);

    ForumCategory editCategory(ForumCategory forumCategory);

    ForumCategory getCategory(Long id);

    void removeCategoryAndForums(Long categoryId);

    void removeCategoryAndMoveForums(Long categoryId, Long newCategoryId);

    Forum addForum(Forum forum, ForumCategory category);

    Forum moveForumToPosition(Forum forum, Integer position);

    Forum moveForumToAnotherCategory(Long forumId, Long categoryId);

    Forum editForum(Forum forum);

    void removeForum(Long forumId);


}
