/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.impl.statistics;

import org.jbb.board.api.forum.Forum;
import org.jbb.posting.api.PostingStatisticsService;
import org.jbb.posting.api.base.Post;
import org.jbb.posting.api.base.PostingStatistics;
import org.jbb.posting.api.exception.PostForumNotFoundException;
import org.jbb.posting.impl.base.ForumProvider;
import org.jbb.posting.impl.base.PostTranslator;
import org.jbb.posting.impl.base.dao.PostRepository;
import org.jbb.posting.impl.base.dao.TopicRepository;
import org.jbb.posting.impl.base.model.TopicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultPostingStatisticsService implements PostingStatisticsService {
    private final ForumProvider forumProvider;

    private final TopicRepository topicRepository;
    private final PostRepository postRepository;

    private final PostTranslator postTranslator;

    @Override
    public PostingStatistics getPostingStatistics(Long forumId) throws PostForumNotFoundException {
        Forum forum = forumProvider.getForum(forumId);
        return PostingStatistics.builder()
                .topicsTotal(topicRepository.countByForumId(forum.getId()))
                .postsTotal(postRepository.countPostByForum(forum.getId()))
                .lastPost(Optional.ofNullable(fetchLastPost(forum.getId())))
                .build();
    }

    private Post fetchLastPost(Long forumId) {
        Page<TopicEntity> lastTopicPage = topicRepository.findByForumId(forumId, PageRequest.of(0, 1, new Sort(Sort.Direction.DESC, "updateDateTime")));
        if (lastTopicPage.getTotalElements() == 0) {
            return null;
        }
        TopicEntity lastTopic = lastTopicPage.getContent().get(0);
        return postTranslator.toModel(lastTopic.getLastPost());
    }
}
