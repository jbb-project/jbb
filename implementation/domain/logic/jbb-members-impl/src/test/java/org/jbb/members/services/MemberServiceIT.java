/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.services;

import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.IPAddress;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.members.CoreConfigMocks;
import org.jbb.members.MembersConfig;
import org.jbb.members.api.model.DisplayedName;
import org.jbb.members.api.model.Login;
import org.jbb.members.api.model.MemberRegistrationAware;
import org.jbb.members.dao.MemberRepository;
import org.jbb.members.entities.MemberEntity;
import org.jbb.members.entities.RegistrationMetaDataEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfigMocks.class, MembersConfig.class, PropertiesConfig.class,
        EventBusConfig.class, DbConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MemberServiceIT {
    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private MemberRepository repository;

    @Test
    public void shouldReturnMembersSortedByRegistrationDate_whenMembersHadSavedNotAscendingByRegistrationDate() throws Exception {
        // given
        // note that we're saving members not ascending by join date
        repository.save(memberJoinedOneMinuteAgo());
        repository.save(memberJoinedForTwoWeeks());
        repository.save(memberJoinedFiveMonthsAgo());

        // when
        List<MemberRegistrationAware> members = memberService.getAllMembersSortedByRegistrationDate();
        Iterator<MemberRegistrationAware> membersIterator = members.iterator();

        // then
        assertThat(membersIterator.next().getDisplayedName().toString()).isEqualTo("John");
        assertThat(membersIterator.next().getDisplayedName().toString()).isEqualTo("Mark");
        assertThat(membersIterator.next().getDisplayedName().toString()).isEqualTo("Tom");
        assertThat(membersIterator.hasNext()).isFalse();
    }

    @Test
    public void shouldReturnEmptyList_whenThereIsNoMember() throws Exception {
        // when
        List<MemberRegistrationAware> members = memberService.getAllMembersSortedByRegistrationDate();

        // then
        assertThat(members).isEmpty();
    }

    private MemberEntity memberJoinedFiveMonthsAgo() {
        return MemberEntity.builder()
                .login(Login.builder().value("john").build())
                .displayedName(DisplayedName.builder().value("John").build())
                .email(Email.builder().value("john@john.com").build())
                .registrationMetaData(RegistrationMetaDataEntity.builder()
                        .joinDateTime(LocalDateTime.now().minusMonths(5))
                        .ipAddress(IPAddress.builder().value("127.0.0.1").build()
                        ).build())
                .build();
    }

    private MemberEntity memberJoinedOneMinuteAgo() {
        return MemberEntity.builder()
                .login(Login.builder().value("mark").build())
                .displayedName(DisplayedName.builder().value("Mark").build())
                .email(Email.builder().value("mark@mark.com").build())
                .registrationMetaData(RegistrationMetaDataEntity.builder()
                        .joinDateTime(LocalDateTime.now().minusMinutes(1))
                        .ipAddress(IPAddress.builder().value("127.0.0.1").build()
                        ).build())
                .build();
    }

    private MemberEntity memberJoinedForTwoWeeks() {
        return MemberEntity.builder()
                .login(Login.builder().value("tom").build())
                .displayedName(DisplayedName.builder().value("Tom").build())
                .email(Email.builder().value("tom@tom.com").build())
                .registrationMetaData(RegistrationMetaDataEntity.builder()
                        .joinDateTime(LocalDateTime.now().plusWeeks(2))
                        .ipAddress(IPAddress.builder().value("127.0.0.1").build()
                        ).build())
                .build();
    }
}