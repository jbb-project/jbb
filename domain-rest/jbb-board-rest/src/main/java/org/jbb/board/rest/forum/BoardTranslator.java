/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.rest.forum;

import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.rest.forum.category.FullForumCategoryDto;
import org.jbb.posting.api.PostingStatisticsService;
import org.jbb.posting.api.base.Post;
import org.jbb.posting.api.base.PostAuthor;
import org.jbb.posting.api.base.PostingStatistics;
import org.jbb.posting.api.exception.PostForumNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BoardTranslator {
    private final PostingStatisticsService postingStatisticsService;

    public BoardDto toDto(List<ForumCategory> forumCategories, List<ForumViewParams> targetForumViewDetails) {
        return BoardDto.builder()
                .forumCategories(buildForumCategoriesDto(forumCategories, targetForumViewDetails))
                .build();
    }

    private List<FullForumCategoryDto> buildForumCategoriesDto(List<ForumCategory> forumCategories,
                                                               List<ForumViewParams> targetForumViewDetails) {
        List<FullForumCategoryDto> categories = forumCategories.stream()
                .map(category -> toDto(category, targetForumViewDetails))
                .collect(Collectors.toList());

        if (!categories.isEmpty()) {
            IntStream.range(0, categories.size())
                    .forEach(i -> categories.get(i).setPosition(i));
        }

        return categories;
    }

    private FullForumCategoryDto toDto(ForumCategory category, List<ForumViewParams> targetForumViewDetails) {
        return FullForumCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .forums(buildForumsDto(category, targetForumViewDetails))
                .build();
    }

    private List<FullForumDto> buildForumsDto(ForumCategory category, List<ForumViewParams> targetForumViewDetails) {
        List<FullForumDto> forums = category.getForums().stream()
                .map(forum -> toDto(forum, targetForumViewDetails))
                .collect(Collectors.toList());

        if (!forums.isEmpty()) {
            IntStream.range(0, forums.size())
                    .forEach(i -> forums.get(i).setPosition(i));
        }

        return forums;
    }

    private FullForumDto toDto(Forum forum, List<ForumViewParams> targetForumViewDetails) {
        FullForumDto forumDto = FullForumDto.builder().id(forum.getId())
                .details(buildDetails(forum))
                .build();
        if (!targetForumViewDetails.isEmpty()) {
            PostingStatistics statistics = getStatistics(forum);
            if (targetForumViewDetails.contains(ForumViewParams.STATISTICS)) {
                forumDto.setStatistics(buildStatistics(statistics));
            }
            if (targetForumViewDetails.contains(ForumViewParams.LAST_POST)) {
                forumDto.setLastPost(buildLastPostDetails(statistics));
            }
        }
        return forumDto;
    }

    private ForumDetailsDto buildDetails(Forum forum) {
        return ForumDetailsDto.builder()
                .name(forum.getName())
                .description(forum.getDescription())
                .closed(forum.isClosed())
                .build();
    }

    private PostingStatistics getStatistics(Forum forum) {
        try {
            return postingStatisticsService.getPostingStatistics(forum.getId());
        } catch (PostForumNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private ForumStatisticsDto buildStatistics(PostingStatistics postingStatistics) {
        return ForumStatisticsDto.builder()
                .totalPosts(postingStatistics.getPostsTotal())
                .totalTopics(postingStatistics.getTopicsTotal())
                .build();
    }

    private ForumLastPostDetailsDto buildLastPostDetails(PostingStatistics postingStatistics) {
        Optional<Post> lastPost = postingStatistics.getLastPost();
        return ForumLastPostDetailsDto.builder()
                .topicId(lastPost.map(Post::getTopicId).orElse(null))
                .postedAt(lastPost.map(Post::getPostedAt).orElse(null))
                .authorMemberId(lastPost.map(this::extractMemberId).orElse(null))
                .anonymousAuthorName(lastPost.map(this::extractAnonymousAuthorName).orElse(null))
                .build();
    }

    private Long extractMemberId(Post post) {
        PostAuthor author = post.getAuthor();
        if (author.isMember()) {
            return author.getAuthorMemberId();
        }
        return null;
    }

    private String extractAnonymousAuthorName(Post post) {
        PostAuthor author = post.getAuthor();
        if (author.isAnonymous()) {
            return author.getAnonAuthorName();
        }
        return null;
    }
}
