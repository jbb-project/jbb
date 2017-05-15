/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.web.forum;

import org.jbb.board.api.model.Forum;
import org.jbb.board.api.model.ForumCategory;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TestbedForumCategory implements ForumCategory {
    private Long id;
    private String name;
    private List<Forum> forums;
}
