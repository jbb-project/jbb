/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.impl;

import lombok.RequiredArgsConstructor;
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

    @Override
    public Post createPost(Long topicId, PostDraft post) throws TopicNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Post editPost(Long postId, PostDraft post) throws PostNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removePost(Long postId) throws PostNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Post getPostChecked(Long postId) throws PostNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FullPost getFullPostChecked(Long postId) throws PostNotFoundException {
        throw new UnsupportedOperationException();
    }
}
