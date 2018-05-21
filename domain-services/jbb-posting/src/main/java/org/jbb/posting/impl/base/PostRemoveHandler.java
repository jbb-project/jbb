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

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.posting.event.PostRemovedEvent;
import org.jbb.posting.event.TopicChangedEvent;
import org.jbb.posting.event.TopicRemovedEvent;
import org.jbb.posting.impl.base.dao.PostRepository;
import org.jbb.posting.impl.base.dao.TopicRepository;
import org.jbb.posting.impl.base.model.PostEntity;
import org.jbb.posting.impl.base.model.TopicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostRemoveHandler {

    private final PostRepository postRepository;
    private final TopicRepository topicRepository;

    private final JbbEventBus eventBus;

    public void removePost(PostEntity postToRemove) {
        TopicEntity topic = postToRemove.getTopic();
        if (removingFirstPostOfTopic(topic, postToRemove)) {
            updateTopicFirstPost(topic);
        } else if (removingLastPostOfTopic(topic, postToRemove)) {
            updateTopicLastPost(topic);
        } else {
            eventBus.post(new TopicChangedEvent(topic.getId()));
        }
        postRepository.delete(postToRemove);
        eventBus.post(new PostRemovedEvent(postToRemove.getId()));
    }

    private boolean removingFirstPostOfTopic(TopicEntity topic, PostEntity postToRemove) {
        return topic.getFirstPost().getId().equals(postToRemove.getId());
    }

    private boolean removingLastPostOfTopic(TopicEntity topic, PostEntity postToRemove) {
        return topic.getLastPost().getId().equals(postToRemove.getId());
    }

    private void updateTopicFirstPost(TopicEntity topic) {
        Page<PostEntity> firstTwoPosts = postRepository
            .findByTopic(topic, PageRequest.of(0, 2, Direction.ASC, "createDateTime"));
        if (firstTwoPosts.getTotalElements() == 1) {
            // remove topic
            PostEntity lastPost = firstTwoPosts.getContent().get(0);
            topicRepository.delete(topic);
            eventBus
                .post(new TopicRemovedEvent(topic.getId(), Lists.newArrayList(lastPost.getId())));
        } else {
            // update first post
            PostEntity secondPost = firstTwoPosts.getContent().get(1);
            topic.setFirstPost(secondPost);
            topicRepository.save(topic);
            eventBus.post(new TopicChangedEvent(topic.getId()));
        }
    }

    private void updateTopicLastPost(TopicEntity topic) {
        Page<PostEntity> lastTwoPosts = postRepository
            .findByTopic(topic, PageRequest.of(0, 2, Direction.DESC, "createDateTime"));
        PostEntity lastButOnePost = lastTwoPosts.getContent().get(1);
        topic.setLastPost(lastButOnePost);
        topicRepository.save(topic);
        eventBus.post(new TopicChangedEvent(topic.getId()));
    }

}
