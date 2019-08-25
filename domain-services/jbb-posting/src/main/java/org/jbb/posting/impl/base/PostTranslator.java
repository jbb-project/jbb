/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.impl.base;

import org.apache.commons.lang3.StringUtils;
import org.jbb.posting.api.base.FullPost;
import org.jbb.posting.api.base.Post;
import org.jbb.posting.api.base.PostAuthor;
import org.jbb.posting.impl.base.model.PostEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostTranslator {

    public Post toModel(PostEntity entity) {
        return Post.builder()
                .id(entity.getId())
                .author(toAuthorModel(entity.getMemberId(), entity.getAnonymousName()))
                .topicId(entity.getTopic().getId())
                .subject(entity.getSubject())
                .postedAt(entity.getCreateDateTime())
                .lastEditedAt(Optional.empty())//todo not supported yet
                .build();
    }

    public FullPost toFullModel(PostEntity entity) {
        return FullPost.fullBuilder()
                .id(entity.getId())
                .author(toAuthorModel(entity.getMemberId(), entity.getAnonymousName()))
                .topicId(entity.getTopic().getId())
                .subject(entity.getSubject())
                .postedAt(entity.getCreateDateTime())
                .lastEditedAt(Optional.empty())//todo not supported yet
                .content(entity.getPostContent().getContent())
                .build();
    }

    private PostAuthor toAuthorModel(Long memberId, String anonymousName) {
        return StringUtils.isBlank(anonymousName) ?
                PostAuthor.ofMember(memberId) : PostAuthor.ofAnonymous(anonymousName);
    }
}
