/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.web.forum.form;

import org.jbb.board.api.forum.Forum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForumForm {
    private Long id;

    private String name;

    private String description;

    private Boolean closed;

    private Long categoryId;

    public Forum buildForum() {
        return new Forum() {
            @Override
            public Long getId() {
                return id;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public Boolean isClosed() {
                return closed;
            }
        };

    }
}
