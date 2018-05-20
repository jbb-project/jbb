/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.rest.post;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.posting.api.base.EditPostDraft;
import org.jbb.posting.api.base.Post;
import org.jbb.posting.api.base.PostAuthor;
import org.jbb.posting.api.base.PostDraft;
import org.jbb.posting.rest.post.exception.DeletePostNotPossible;
import org.jbb.posting.rest.post.exception.MemberFilledAnonymousName;
import org.jbb.posting.rest.post.exception.UpdatePostNotPossible;
import org.jbb.posting.rest.topic.exception.DeleteTopicNotPossible;
import org.jbb.security.api.privilege.PrivilegeService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostModelTranslator {

    private final MemberService memberService;
    private final PrivilegeService privilegeService;

    public PostDraft toPostModel(CreateUpdatePostDto dto) {
        Optional<Member> currentMember = memberService.getCurrentMember();
        String anonymousName = dto.getAnonymousName();
        if (currentMember.isPresent() && StringUtils.isBlank(anonymousName)) {
            throw new MemberFilledAnonymousName();
        }

        return PostDraft.builder()
            .author(buildAuthor(anonymousName, currentMember.orElse(null)))
            .subject(dto.getSubject())
            .content(dto.getContent())
            .build();
    }

    private PostAuthor buildAuthor(String anonymousName, Member currentMember) {
        return currentMember == null ?
            PostAuthor.ofAnonymous(anonymousName) : PostAuthor.ofMember(currentMember.getId());
    }

    public EditPostDraft toEditPostModel(CreateUpdatePostDto dto, Post post) {
        Optional<Member> currentMember = memberService.getCurrentMember();
        String anonymousName = dto.getAnonymousName();
        if (currentMember.isPresent()) {
            Long currentMemberId = currentMember.get().getId();
            if (StringUtils.isBlank(anonymousName)) {
                throw new MemberFilledAnonymousName();
            }
            PostAuthor author = post.getAuthor();
            if (!privilegeService.hasAdministratorPrivilege(currentMemberId)) {
                if (author.isMember() && !author.getAuthorMemberId().equals(currentMemberId)) {
                    throw new UpdatePostNotPossible();
                }
            }
        } else {
            throw new UpdatePostNotPossible();
        }

        return EditPostDraft.builder()
            .subject(dto.getSubject())
            .content(dto.getContent())
            .build();
    }

    public void assertDeletePostPrivileges() {
        assertDeletePrivileges(new DeletePostNotPossible());
    }

    public void assertDeleteTopicPrivileges() {
        assertDeletePrivileges(new DeleteTopicNotPossible());
    }

    private void assertDeletePrivileges(RuntimeException ex) {
        Optional<Member> currentMember = memberService.getCurrentMember();
        if (currentMember.isPresent()) {
            Long currentMemberId = currentMember.get().getId();
            if (privilegeService.hasAdministratorPrivilege(currentMemberId)) {
                return;
            }
        }
        throw ex;
    }
}
