/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.rest.forum;

import org.jbb.board.api.forum.Forum;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ForumTranslator {

    public List<ForumDto> toDto(List<Forum> forums) {
        return null;
    }

    public Forum toModel(CreateUpdateForumDto forumDto) {
        return null;
    }

    public ForumDto toDto(Forum forum) {
        return null;
    }
}
