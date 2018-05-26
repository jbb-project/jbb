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

import org.apache.commons.lang3.Validate;
import org.jbb.board.api.forum.Forum;
import org.jbb.lib.db.domain.BaseEntity;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.posting.api.TopicService;
import org.jbb.posting.api.base.FullPost;
import org.jbb.posting.api.base.Post;
import org.jbb.posting.api.base.PostDraft;
import org.jbb.posting.api.base.Topic;
import org.jbb.posting.api.exception.PostForumNotFoundException;
import org.jbb.posting.api.exception.TopicNotFoundException;
import org.jbb.posting.event.PostCreatedEvent;
import org.jbb.posting.event.TopicCreatedEvent;
import org.jbb.posting.event.TopicRemovedEvent;
import org.jbb.posting.impl.base.dao.PostRepository;
import org.jbb.posting.impl.base.dao.TopicRepository;
import org.jbb.posting.impl.base.model.PostEntity;
import org.jbb.posting.impl.base.model.TopicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultTopicService implements TopicService {

    private final ForumProvider forumProvider;

    private final PostCreator postCreator;
    private final PostTranslator postTranslator;
    private final PostRepository postRepository;

    private final TopicTranslator topicTranslator;
    private final TopicRepository topicRepository;

    private final JbbEventBus eventBus;

    @Override
    @Transactional
    public Topic createTopic(Long forumId, PostDraft draft) throws PostForumNotFoundException {
        Validate.notNull(forumId);
        Validate.notNull(draft);
        Forum forum = forumProvider.getForum(forumId);
        PostEntity post = postCreator.toEntity(draft);

        TopicEntity topic = TopicEntity.builder()
            .forumId(forum.getId())
            .build();

        post.setTopic(topic);
        post = postRepository.save(post);
        topic = post.getTopic();
        topic.setFirstPost(post);
        topic.setLastPost(post);
        topic = topicRepository.save(topic);
        eventBus.post(new TopicCreatedEvent(topic.getId()));
        eventBus.post(new PostCreatedEvent(post.getId()));
        return topicTranslator.toModel(topic);
    }

    @Override
    @Transactional
    public void removeTopic(Long topicId) throws TopicNotFoundException {
        Validate.notNull(topicId);
        TopicEntity topic = topicRepository.findById(topicId)
            .orElseThrow(() -> new TopicNotFoundException(topicId));
        removePosts(topic);
        topicRepository.delete(topic);
        eventBus.post(new TopicRemovedEvent(topicId));
    }

    @Transactional
    public List<Long> removePosts(TopicEntity topic) {
        List<PostEntity> posts = postRepository.findByTopic(topic);
        List<Long> postIds = posts.stream().map(BaseEntity::getId).collect(Collectors.toList());
        posts.forEach(post -> {
            post.setTopic(null);
            postRepository.delete(post);
        });
        return postIds;
    }

    @Override
    @Transactional(readOnly = true)
    public Topic getTopic(Long topicId) throws TopicNotFoundException {
        Validate.notNull(topicId);
        TopicEntity topic = topicRepository.findById(topicId)
            .orElseThrow(() -> new TopicNotFoundException(topicId));
        return topicTranslator.toModel(topic);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Topic> getForumTopics(Long forumId, PageRequest pageRequest)
        throws PostForumNotFoundException {
        Validate.notNull(forumId);
        Validate.notNull(pageRequest);
        Forum forum = forumProvider.getForum(forumId);
        return topicRepository.findByForumId(forum.getId(), pageRequest)
            .map(topicTranslator::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsForTopic(Long topicId, PageRequest pageRequest)
        throws TopicNotFoundException {
        Validate.notNull(topicId);
        Validate.notNull(pageRequest);
        TopicEntity topic = topicRepository.findById(topicId)
            .orElseThrow(() -> new TopicNotFoundException(topicId));
        return postRepository.findByTopic(topic, pageRequest)
            .map(postTranslator::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FullPost> getFullPostsForTopic(Long topicId, PageRequest pageRequest)
        throws TopicNotFoundException {
        Validate.notNull(topicId);
        Validate.notNull(pageRequest);
        TopicEntity topic = topicRepository.findById(topicId)
            .orElseThrow(() -> new TopicNotFoundException(topicId));
        return postRepository.findByTopic(topic, pageRequest)
            .map(postTranslator::toFullModel);
    }
}
