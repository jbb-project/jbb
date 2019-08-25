/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.impl.base;

import com.google.common.eventbus.Subscribe;

import org.jbb.lib.eventbus.JbbEventBusListener;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.event.MemberRemovedEvent;
import org.jbb.posting.impl.base.dao.PostRepository;
import org.jbb.posting.impl.base.model.PostEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberRemovedEventHandler implements JbbEventBusListener {
    private final PostRepository postRepository;

    private final MemberService memberService;

    @Subscribe
    @Transactional
    public void updateMemberPosts(MemberRemovedEvent event) throws MemberNotFoundException {
        log.debug("Update posts author for removed member with id: {}",
                event.getMemberId());
        Member removedMember = memberService.getMemberWithIdChecked(event.getMemberId());
        postRepository.findByMemberId(event.getMemberId())
                .forEach(post -> updateAuthor(post, removedMember.getDisplayedName()));
    }

    private void updateAuthor(PostEntity post, DisplayedName displayedName) {
        post.setAnonymousName(displayedName.getValue());
        post.setMemberId(null);
        postRepository.save(post);
    }
}
