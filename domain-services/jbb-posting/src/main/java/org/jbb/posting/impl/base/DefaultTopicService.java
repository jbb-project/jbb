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
import org.jbb.board.api.forum.ForumService;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.posting.api.TopicService;
import org.jbb.posting.api.base.FullPost;
import org.jbb.posting.api.base.PostDraft;
import org.jbb.posting.api.base.Topic;
import org.jbb.posting.api.exception.PostForumNotFoundException;
import org.jbb.posting.api.exception.TopicNotFoundException;
import org.jbb.posting.impl.base.dao.TopicRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultTopicService implements TopicService {

    private final ForumService forumService;
    private final JbbEventBus eventBus;
    private TopicRepository topicRepository;

    @Override
    public Topic createTopic(Long forumId, PostDraft post) throws PostForumNotFoundException {
        Validate.notNull(forumId);
        Validate.notNull(post);
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeTopic(Long topicId) throws TopicNotFoundException {
        Validate.notNull(topicId);
        throw new UnsupportedOperationException();
    }

    @Override
    public Topic getTopic(Long topicId) throws TopicNotFoundException {
        Validate.notNull(topicId);
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<Topic> getForumTopics(Long forumId, PageRequest pageRequest)
        throws PostForumNotFoundException {
        Validate.notNull(forumId);
        Validate.notNull(pageRequest);
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<FullPost> getFullPostsForTopic(Long topicId, PageRequest pageRequest)
        throws TopicNotFoundException {
        Validate.notNull(topicId);
        Validate.notNull(pageRequest);
        throw new UnsupportedOperationException();
    }
}
