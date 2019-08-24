/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.controller;

import org.jbb.board.api.forum.BoardService;
import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.frontend.web.base.data.ForumCategoryRow;
import org.jbb.frontend.web.base.data.ForumRow;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberService;
import org.jbb.posting.api.PostingStatisticsService;
import org.jbb.posting.api.TopicService;
import org.jbb.posting.api.base.Post;
import org.jbb.posting.api.base.PostAuthor;
import org.jbb.posting.api.base.PostingStatistics;
import org.jbb.posting.api.base.Topic;
import org.jbb.posting.api.exception.PostForumNotFoundException;
import org.jbb.posting.api.exception.TopicNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomePageController {
    private static final String HOME_VIEW_NAME = "home";

    private final BoardService boardService;
    private final PostingStatisticsService postingStatisticsService;
    private final TopicService topicService;
    private final MemberService memberService;

    @RequestMapping("/")
    public String main(Model model) {
        List<ForumCategory> forumCategories = boardService.getForumCategories();

        List<ForumCategoryRow> forumStructureRows = forumCategories.stream()
                .map(this::createDto)
                .collect(Collectors.toList());

        model.addAttribute("forumStructure", forumStructureRows);

        return HOME_VIEW_NAME;
    }

    private ForumCategoryRow createDto(ForumCategory forumCategory) {
        ForumCategoryRow categoryRow = new ForumCategoryRow();
        categoryRow.setName(forumCategory.getName());

        List<ForumRow> forumRows = forumCategory.getForums().stream()
                .map(this::createDto)
                .collect(Collectors.toList());

        categoryRow.setForumRows(forumRows);
        return categoryRow;
    }

    private ForumRow createDto(Forum forum) {
        PostingStatistics postingStatistics;
        Optional<Post> lastPost;
        Optional<String> topicName = Optional.empty();
        Optional<String> authorName = Optional.empty();
        try {
            postingStatistics = postingStatisticsService.getPostingStatistics(forum.getId());
            lastPost = postingStatistics.getLastPost();
            if (lastPost.isPresent()) {
                Topic topic = topicService.getTopic(lastPost.get().getTopicId());
                topicName = Optional.of(topic.getSubject());

                PostAuthor author = lastPost.get().getAuthor();
                if (author.isAnonymous()) {
                    authorName = Optional.of(author.getAnonAuthorName());
                } else {
                    Member member = memberService.getMemberWithIdChecked(author.getAuthorMemberId());
                    authorName = Optional.of(member.getDisplayedName().getValue());
                }
            }
        } catch (PostForumNotFoundException | TopicNotFoundException | MemberNotFoundException e) {
            throw new IllegalStateException(e);
        }
        return ForumRow.builder()
                .id(forum.getId())
                .name(forum.getName())
                .description(forum.getDescription())
                .closed(forum.isClosed())
                .totalPosts(postingStatistics.getPostsTotal())
                .totalTopics(postingStatistics.getTopicsTotal())
                .lastPostMemberName(authorName.orElse("-"))
                .lastPostTopic(topicName.orElse("-"))
                .lastPostAt(lastPost.map(Post::getPostedAt).orElse(null))
                .build();
    }

}
