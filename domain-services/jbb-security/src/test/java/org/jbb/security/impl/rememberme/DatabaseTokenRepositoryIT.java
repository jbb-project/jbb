/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.rememberme;

import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.impl.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class DatabaseTokenRepositoryIT extends BaseIT {

    @Autowired
    private MemberService memberServiceMock;

    @Autowired
    private DatabaseTokenRepository databaseTokenRepository;

    @Before
    public void setUp() {
        Mockito.reset(memberServiceMock);
    }

    @Test
    public void fullCrudTest() {
        // given
        Member member = exampleMember();
        given(memberServiceMock.getMemberWithUsername(eq(Username.of("john")))).willReturn(
                Optional.of(member));
        given(memberServiceMock.getMemberWithId(eq(33L))).willReturn(Optional.of(member));

        PersistentRememberMeToken rememberMeToken = new PersistentRememberMeToken("john", "series", "tokenVal", new Date());

        // when
        databaseTokenRepository.createNewToken(rememberMeToken);
        PersistentRememberMeToken token = databaseTokenRepository.getTokenForSeries("series");

        // then
        assertThat(token.getTokenValue()).isEqualTo(rememberMeToken.getTokenValue());

        // when
        databaseTokenRepository.updateToken("series", "newTokenVal", new Date());
        PersistentRememberMeToken newToken = databaseTokenRepository.getTokenForSeries("series");

        // then
        assertThat(newToken.getTokenValue()).isEqualTo("newTokenVal");

        // when
        databaseTokenRepository.removeUserTokens("john");
        PersistentRememberMeToken johnToken = databaseTokenRepository.getTokenForSeries("series");

        // then
        assertThat(johnToken).isNull();
    }

    private Member exampleMember() {
        Member member = mock(Member.class);
        given(member.getId()).willReturn(33L);
        given(member.getUsername()).willReturn(Username.of("john"));
        return member;
    }
}