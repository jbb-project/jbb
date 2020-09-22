/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.api;

import org.jbb.posting.api.base.EditPostDraft;
import org.jbb.posting.api.base.FullPost;
import org.jbb.posting.api.base.Post;
import org.jbb.posting.api.base.PostDraft;
import org.jbb.posting.api.exception.PostNotFoundException;
import org.jbb.posting.api.exception.TopicNotFoundException;

public interface PostingService {

    Post createPost(Long topicId, PostDraft post) throws TopicNotFoundException;

    Post editPost(Long postId, EditPostDraft post) throws PostNotFoundException;

    void removePost(Long postId) throws PostNotFoundException;

    Post getPost(Long postId) throws PostNotFoundException;

    FullPost getFullPost(Long postId) throws PostNotFoundException;

}
