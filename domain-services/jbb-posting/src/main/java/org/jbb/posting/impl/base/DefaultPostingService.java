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
import org.apache.commons.lang3.Validate;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.posting.api.PostingService;
import org.jbb.posting.api.base.FullPost;
import org.jbb.posting.api.base.Post;
import org.jbb.posting.api.base.PostDraft;
import org.jbb.posting.api.exception.PostNotFoundException;
import org.jbb.posting.api.exception.TopicNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultPostingService implements PostingService {

    private final JbbEventBus eventBus;

    @Override
    public Post createPost(Long topicId, PostDraft post) throws TopicNotFoundException {
        Validate.notNull(topicId);
        Validate.notNull(post);
        throw new UnsupportedOperationException();
    }

    @Override
    public Post editPost(Long postId, PostDraft post) throws PostNotFoundException {
        Validate.notNull(postId);
        Validate.notNull(post);
        throw new UnsupportedOperationException();
    }

    @Override
    public void removePost(Long postId) throws PostNotFoundException {
        Validate.notNull(postId);
        throw new UnsupportedOperationException();
    }

    @Override
    public Post getPost(Long postId) throws PostNotFoundException {
        Validate.notNull(postId);
        throw new UnsupportedOperationException();
    }

    @Override
    public FullPost getFullPost(Long postId) throws PostNotFoundException {
        Validate.notNull(postId);
        throw new UnsupportedOperationException();
    }
}
