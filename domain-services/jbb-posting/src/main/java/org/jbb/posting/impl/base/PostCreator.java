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

import org.jbb.posting.api.base.PostAuthor;
import org.jbb.posting.api.base.PostDraft;
import org.jbb.posting.impl.base.model.PostContentEntity;
import org.jbb.posting.impl.base.model.PostDocument;
import org.jbb.posting.impl.base.model.PostEntity;
import org.springframework.stereotype.Component;

@Component
public class PostCreator {

    public PostEntity toEntity(PostDraft postDraft) {
        PostAuthor author = postDraft.getAuthor();
        return PostEntity.builder()
            .anonymousName(author.isAnonymous() ? author.getAnonAuthorName() : null)
            .memberId(author.isMember() ? author.getAuthorMemberId() : null)
            .subject(postDraft.getSubject())
            .postContent(PostContentEntity.builder()
                .content(postDraft.getContent())
                .build())
            .build();
    }

    public PostDocument toDocument(PostDraft postDraft, Long id) {
        return PostDocument.builder()
                .id(id.toString())
                .authorName(postDraft.getAuthor().getAnonAuthorName())
                .content(postDraft.getContent())
                .build();

    }

}
