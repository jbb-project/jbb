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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.impl.rememberme.dao.PersistentLoginRepository;
import org.jbb.security.impl.rememberme.model.PersistentLoginEntity;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DatabaseTokenRepository implements PersistentTokenRepository {

    private final PersistentLoginRepository persistentLoginRepository;
    private final MemberService memberService;

    @Override
    public void createNewToken(PersistentRememberMeToken persistentRememberMeToken) {
        Member member = memberService
            .getMemberWithUsername(Username.of(persistentRememberMeToken.getUsername()))
            .orElseThrow(() -> new IllegalStateException("Member with username not found"));
        PersistentLoginEntity token = PersistentLoginEntity.builder()
            .memberId(member.getId())
            .series(persistentRememberMeToken.getSeries())
            .token(persistentRememberMeToken.getTokenValue())
            .lastUsed(toLocalDateTime(persistentRememberMeToken.getDate()))
            .build();
        persistentLoginRepository.save(token);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        PersistentLoginEntity login = persistentLoginRepository.findBySeries(series);
        login.setToken(tokenValue);
        login.setLastUsed(toLocalDateTime(lastUsed));
        persistentLoginRepository.save(login);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        PersistentLoginEntity login = persistentLoginRepository.findBySeries(seriesId);
        if (login == null) {
            return null;
        }
        Member member = memberService.getMemberWithId(login.getMemberId())
            .orElseThrow(() -> new IllegalStateException("Member with id not found"));
        return new PersistentRememberMeToken(
            member.getUsername().getValue(),
            login.getSeries(),
            login.getToken(),
            Date.from(login.getLastUsed().atZone(ZoneId.systemDefault()).toInstant())
        );
    }

    @Override
    public void removeUserTokens(String username) {
        Member member = memberService
            .getMemberWithUsername(Username.of(username))
            .orElseThrow(() -> new IllegalStateException("Member with username not found"));
        persistentLoginRepository.findByMemberId(member.getId())
            .forEach(persistentLoginRepository::delete);
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }
}
