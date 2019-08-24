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
import org.jbb.posting.api.base.Post;
import org.jbb.posting.api.base.Topic;
import org.jbb.posting.impl.base.model.TopicEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicTranslator {

    private final PostTranslator postTranslator;

    public Topic toModel(TopicEntity entity) {
        Post firstPost = postTranslator.toModel(entity.getFirstPost());
        Post lastPost = postTranslator.toModel(entity.getLastPost());
        return Topic.builder()
            .id(entity.getId())
            .forumId(entity.getForumId())
            .firstPost(firstPost)
            .lastPost(lastPost)
            .build();

    }

}
