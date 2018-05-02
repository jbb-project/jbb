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
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
public class ForumTranslator {

    public ForumsDto toDto(List<Forum> forums) {
        return ForumsDto.builder()
                .forums(forums.stream()
                        .map(this::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public Forum toModel(CreateUpdateForumDto dto, Long forumId) {
        return ForumImpl.builder()
                .id(forumId)
                .name(dto.getName())
                .description(dto.getDescription())
                .closed(dto.getClosed())
                .build();
    }

    public ForumDto toDto(Forum forum) {
        return ForumDto.builder()
                .id(forum.getId())
                .name(forum.getName())
                .description(forum.getDescription())
                .closed(forum.isClosed())
                .build();
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ForumImpl implements Forum {
        private Long id;
        private String name;
        private String description;
        private Boolean closed;

        @Override
        public Boolean isClosed() {
            return closed;
        }
    }
}
