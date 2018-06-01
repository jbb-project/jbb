/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.impl.base.install;

import com.github.zafarkhaja.semver.Version;

import org.jbb.board.api.forum.BoardService;
import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.registration.MemberRegistrationAware;
import org.jbb.posting.api.TopicService;
import org.jbb.posting.api.base.PostAuthor;
import org.jbb.posting.api.base.PostDraft;
import org.jbb.posting.api.exception.PostForumNotFoundException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExampleTopicAction implements InstallUpdateAction {

    private final BoardService boardService;
    private final TopicService topicService;
    private final MemberService memberService;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_12_0;
    }

    @Override
    public void install(InstallationData installationData) {
        MemberRegistrationAware adminMember = memberService.getAllMembersSortedByRegistrationDate().get(0);
        ForumCategory firstCategory = boardService.getForumCategories().get(0);
        Forum firstForum = firstCategory.getForums().get(0);
        PostDraft postDraft = PostDraft.builder()
                .subject("Welcome to board!")
                .author(PostAuthor.ofMember(adminMember.getId()))
                .content("This is an example post in your jBB installation. Everything seems to be working. Have fun!")
                .build();
        try {
            topicService.createTopic(firstForum.getId(), postDraft);
        } catch (PostForumNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

}
