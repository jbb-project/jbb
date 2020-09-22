/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.rest.post;

import org.jbb.posting.api.base.FullPost;
import org.jbb.posting.api.base.Post;
import org.jbb.posting.api.base.PostAuthor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostDtoTranslator {

    public PostDto toDto(Post post) {
        PostAuthor author = post.getAuthor();
        return PostDto.builder()
                .id(post.getId())
                .topicId(post.getTopicId())
                .authorMemberId(author.isMember() ? author.getAuthorMemberId() : null)
                .anonymousName(author.isAnonymous() ? author.getAnonAuthorName() : null)
                .subject(post.getSubject())
                .postedAt(post.getPostedAt())
                .build();
    }

    public PostContentDto toContentDto(FullPost post) {
        PostAuthor author = post.getAuthor();
        return PostContentDto.builder()
                .id(post.getId())
                .topicId(post.getTopicId())
                .authorMemberId(author.isMember() ? author.getAuthorMemberId() : null)
                .anonymousName(author.isAnonymous() ? author.getAnonAuthorName() : null)
                .subject(post.getSubject())
                .postedAt(post.getPostedAt())
                .content(post.getContent())
                .build();
    }

}
