/*
 * Copyright (C) 2019 the original author or authors.
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

    public BoardDto toDto(List<ForumCategory> forumCategories, Boolean includePostingDetails) {
        return BoardDto.builder()
                .forumCategories(buildForumCategoriesDto(forumCategories, includePostingDetails))
                .build();
    }

    private List<FullForumCategoryDto> buildForumCategoriesDto(List<ForumCategory> forumCategories,
                                                               Boolean includePostingDetails) {
        List<FullForumCategoryDto> categories = forumCategories.stream()
                .map(category -> toDto(category, includePostingDetails))
                .collect(Collectors.toList());

        if (!categories.isEmpty()) {
            IntStream.range(0, categories.size())
                    .forEach(i -> categories.get(i).setPosition(i));
        }

        return categories;
    }

    private FullForumCategoryDto toDto(ForumCategory category, Boolean includePostingDetails) {
        return FullForumCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .forums(buildForumsDto(category, includePostingDetails))
                .build();
    }

    private List<FullForumDto> buildForumsDto(ForumCategory category, Boolean includePostingDetails) {
        List<FullForumDto> forums = category.getForums().stream()
                .map(forum -> toDto(forum, includePostingDetails))
                .collect(Collectors.toList());

        if (!forums.isEmpty()) {
            IntStream.range(0, forums.size())
                    .forEach(i -> forums.get(i).setPosition(i));
        }

        return forums;
    }

    private FullForumDto toDto(Forum forum, Boolean includePostingDetails) {
        FullForumDto forumDto = FullForumDto.builder().id(forum.getId())
                .name(forum.getName())
                .description(forum.getDescription())
                .closed(forum.isClosed())
                .build();
        if (includePostingDetails) {
            ForumPostingDetailsDto detailsDto = toPostingDetailsDto(forum);
            forumDto.setPostingDetails(detailsDto);
        }
        return forumDto;
    }

    public ForumPostingDetailsDto toPostingDetailsDto(Forum forum) {
        PostingStatistics statistics = getStatistics(forum);
        return ForumPostingDetailsDto.builder()
                .statistics(buildStatistics(statistics))
                .lastPost(buildLastPostDetails(statistics))
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
