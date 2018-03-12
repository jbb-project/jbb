/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.rest.forum.category;

import com.google.common.collect.Lists;

import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumCategory;
import org.springframework.stereotype.Component;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
public class ForumCategoryTranslator {

    public ForumCategoryDto toDto(ForumCategory forumCategory) {
        return ForumCategoryDto.builder()
                .id(forumCategory.getId())
                .name(forumCategory.getName())
                .build();
    }

    public ForumCategory toModel(CreateUpdateForumCategoryDto dto, Long categoryId) {
        return ForumCategoryImpl.builder()
                .id(categoryId)
                .name(dto.getName())
                .build();
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ForumCategoryImpl implements ForumCategory {

        private Long id;
        private String name;
        @Builder.Default
        private List<Forum> forums = Lists.newArrayList();

    }
}
