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

import java.util.Optional;

public interface ForumCategoryService {

    ForumCategory addCategory(ForumCategory forumCategory);

    ForumCategory moveCategoryToPosition(ForumCategory forumCategory, Integer position);

    ForumCategory editCategory(ForumCategory forumCategory);

    Optional<ForumCategory> getCategory(Long id);

    ForumCategory getCategoryWithForum(Forum forum);

    void removeCategoryAndForums(Long categoryId);

    void removeCategoryAndMoveForums(Long categoryId, Long newCategoryId);
}
