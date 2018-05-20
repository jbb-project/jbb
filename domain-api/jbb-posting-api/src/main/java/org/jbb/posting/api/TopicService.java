/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.api;

import org.jbb.posting.api.base.FullPost;
import org.jbb.posting.api.base.PostDraft;
import org.jbb.posting.api.base.Topic;
import org.jbb.posting.api.exception.ForumNotFoundException;
import org.jbb.posting.api.exception.TopicNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface TopicService {

    Topic createTopic(Long forumId, PostDraft post) throws ForumNotFoundException;

    void removeTopic(Long topicId) throws TopicNotFoundException;

    Topic getTopic(Long topicId) throws TopicNotFoundException;

    Page<Topic> getForumTopics(Long forumId, PageRequest pageRequest) throws ForumNotFoundException;

    Page<FullPost> getFullPostsForTopic(Long topicId, PageRequest pageRequest)
        throws TopicNotFoundException;

}
