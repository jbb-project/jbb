/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.impl.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumNotFoundException;
import org.jbb.board.api.forum.ForumService;
import org.jbb.posting.api.exception.PostForumNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ForumProvider {

    private final ForumService forumService;

    public Forum getForum(Long forumId) throws PostForumNotFoundException {
        try {
            return forumService.getForum(forumId);
        } catch (ForumNotFoundException ex) {
            log.trace("Forum not found", ex);
            throw new PostForumNotFoundException(forumId);
        }
    }

}
