/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.api.forum;

public interface ForumService {
    Forum getForum(Long id);

    Forum getForumChecked(Long id);

    Forum addForum(Forum forum, ForumCategory category);

    Forum moveForumToPosition(Forum forum, Integer position);

    Forum moveForumToAnotherCategory(Long forumId, Long categoryId);

    Forum editForum(Forum forum);

    void removeForum(Long forumId);
}
