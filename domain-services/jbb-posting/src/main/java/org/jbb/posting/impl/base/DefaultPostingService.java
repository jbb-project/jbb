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
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.posting.api.PostingService;
import org.jbb.posting.api.base.EditPostDraft;
import org.jbb.posting.api.base.FullPost;
import org.jbb.posting.api.base.Post;
import org.jbb.posting.api.base.PostDraft;
import org.jbb.posting.api.exception.PostNotFoundException;
import org.jbb.posting.api.exception.TopicNotFoundException;
import org.jbb.posting.event.PostChangedEvent;
import org.jbb.posting.event.PostCreatedEvent;
import org.jbb.posting.event.TopicChangedEvent;
import org.jbb.posting.impl.base.dao.PostDocumentRepository;
import org.jbb.posting.impl.base.dao.PostRepository;
import org.jbb.posting.impl.base.dao.TopicRepository;
import org.jbb.posting.impl.base.model.PostEntity;
import org.jbb.posting.impl.base.model.TopicEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultPostingService implements PostingService {

    private final TopicRepository topicRepository;

    private final PostCreator postCreator;
    private final PostTranslator postTranslator;
    private final PostRepository postRepository;
    private final PostRemoveHandler postRemoveHandler;

    private final PostDocumentRepository postDocumentRepository;

    private final JbbEventBus eventBus;

    @Override
    @Transactional
    public Post createPost(Long topicId, PostDraft draft) throws TopicNotFoundException {
        Validate.notNull(topicId);
        Validate.notNull(draft);
        TopicEntity topic = topicRepository.findById(topicId)
            .orElseThrow(() -> new TopicNotFoundException(topicId));
        PostEntity post = postCreator.toEntity(draft);
        post.setTopic(topic);
        topic.setLastPost(post);
        post = postRepository.save(post);
        topic = topicRepository.save(topic);
        postDocumentRepository.save(postCreator.toDocument(draft, post.getId()));
        eventBus.post(new PostCreatedEvent(post.getId()));
        eventBus.post(new TopicChangedEvent(topic.getId()));
        return postTranslator.toModel(post);
    }

    @Override
    @Transactional
    public Post editPost(Long postId, EditPostDraft draft) throws PostNotFoundException {
        Validate.notNull(postId);
        Validate.notNull(draft);
        PostEntity post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));
        TopicEntity topic = post.getTopic();

        post.setSubject(draft.getSubject());
        post.getPostContent().setContent(draft.getContent());
        post = postRepository.save(post);

        eventBus.post(new PostChangedEvent(post.getId()));
        eventBus.post(new TopicChangedEvent(topic.getId()));
        return postTranslator.toModel(post);
    }

    @Override
    @Transactional
    public void removePost(Long postId) throws PostNotFoundException {
        Validate.notNull(postId);
        PostEntity post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));
        postRemoveHandler.removePost(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Post getPost(Long postId) throws PostNotFoundException {
        Validate.notNull(postId);
        PostEntity post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));
        return postTranslator.toModel(post);
    }

    @Override
    @Transactional(readOnly = true)
    public FullPost getFullPost(Long postId) throws PostNotFoundException {
        Validate.notNull(postId);
        PostEntity post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));
        return postTranslator.toFullModel(post);
    }
}
