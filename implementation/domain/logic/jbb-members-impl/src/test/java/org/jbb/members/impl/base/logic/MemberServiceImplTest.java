/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.logic;

import com.google.common.collect.Lists;

import org.jbb.lib.core.vo.Username;
import org.jbb.members.api.data.Member;
import org.jbb.members.api.data.MemberRegistrationAware;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MemberServiceImplTest {
    @Mock
    private MemberRepository memberRepositoryMock;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    public void shouldMapToMemberRegistrationAware() throws Exception {
        // given
        MemberEntity memberEntityMock = mock(MemberEntity.class);
        given(memberRepositoryMock.findAllByOrderByRegistrationDateAsc()).willReturn(Lists.newArrayList(memberEntityMock));

        // when
        List<MemberRegistrationAware> members = memberService.getAllMembersSortedByRegistrationDate();

        // then
        assertThat(members).hasSize(1);
    }

    @Test
    public void shouldReturnMember_whenExistWithGivenUsername() throws Exception {
        // given
        Username username = Username.builder().value("john").build();

        given(memberRepositoryMock.findByUsername(eq(username))).willReturn(Optional.of(mock(MemberEntity.class)));

        // when
        Optional<Member> member = memberService.getMemberWithUsername(username);

        // then
        assertThat(member).isPresent();
    }

    @Test
    public void shouldReturnEmpty_whenNotExistWithGivenUsername() throws Exception {
        // given
        Username username = Username.builder().value("john").build();

        given(memberRepositoryMock.findByUsername(eq(username))).willReturn(Optional.empty());

        // when
        Optional<Member> member = memberService.getMemberWithUsername(username);

        // then
        assertThat(member).isEmpty();
    }
}