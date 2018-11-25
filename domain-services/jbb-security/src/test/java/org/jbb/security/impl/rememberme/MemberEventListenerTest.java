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

import org.assertj.core.util.Lists;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.event.MemberRemovedEvent;
import org.jbb.security.impl.rememberme.dao.PersistentLoginRepository;
import org.jbb.security.impl.rememberme.model.PersistentLoginEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class MemberEventListenerTest {

    @Mock
    private PersistentLoginRepository persistentLoginRepositoryMock;

    @InjectMocks
    private MemberEventListener memberEventListener;

    @Test
    public void shouldDeleteAllPersistentLogins_forRemovedMember() throws MemberNotFoundException {
        // given
        PersistentLoginEntity persistentLoginEntity = PersistentLoginEntity.builder()
                .build();
        given(persistentLoginRepositoryMock.findByMemberId(eq(11L))).willReturn(Lists.newArrayList(persistentLoginEntity));

        // when
        memberEventListener.removePersistentLogins(new MemberRemovedEvent(11L));

        // then
        Mockito.verify(persistentLoginRepositoryMock).findByMemberId(eq(11L));
        Mockito.verify(persistentLoginRepositoryMock).delete(eq(persistentLoginEntity));
    }
}