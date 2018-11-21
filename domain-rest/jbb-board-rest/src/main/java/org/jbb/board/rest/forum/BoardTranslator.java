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
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.rest.forum.category.FullForumCategoryDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class BoardTranslator {

    public BoardDto toDto(List<ForumCategory> forumCategories) {
        return BoardDto.builder()
                .forumCategories(buildForumCategoriesDto(forumCategories))
                .build();
    }

    private List<FullForumCategoryDto> buildForumCategoriesDto(List<ForumCategory> forumCategories) {
        List<FullForumCategoryDto> categories = forumCategories.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        if (!categories.isEmpty()) {
            IntStream.range(0, categories.size())
                    .forEach(i -> categories.get(i).setPosition(i));
        }

        return categories;
    }

    private FullForumCategoryDto toDto(ForumCategory category) {
        return FullForumCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .forums(buildForumsDto(category))
                .build();
    }

    private List<FullForumDto> buildForumsDto(ForumCategory category) {
        List<FullForumDto> forums = category.getForums().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        if (!forums.isEmpty()) {
            IntStream.range(0, forums.size())
                    .forEach(i -> forums.get(i).setPosition(i));
        }

        return forums;
    }

    private FullForumDto toDto(Forum forum) {
        return FullForumDto.builder()
                .id(forum.getId())
                .name(forum.getName())
                .description(forum.getDescription())
                .closed(forum.isClosed())
                .build();
    }
}
