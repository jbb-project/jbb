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

import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.IPAddress;
import org.jbb.lib.core.vo.Username;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CleanHsqlDbAfterTestsConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.MemberRegistrationAware;
import org.jbb.members.api.service.MemberService;
import org.jbb.members.impl.MembersConfig;
import org.jbb.members.impl.SecurityConfigMocks;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.members.impl.registration.model.RegistrationMetaDataEntity;
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
@ContextConfiguration(classes = {CoreConfigMocks.class, CleanHsqlDbAfterTestsConfig.class, SecurityConfigMocks.class,
        MembersConfig.class, PropertiesConfig.class,
        EventBusConfig.class, DbConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MemberServiceIT {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository repository;

    @Test
    public void shouldReturnMembersSortedByRegistrationDate_whenMembersHadSavedNotAscendingByRegistrationDate() throws Exception {
        // given
        // note that we're saving members not ascending by join date
        repository.save(memberJoinedOneMinuteAgo());
        repository.save(memberJoinedForTwoWeeks());
        // remember that Administrator registered in real time
        repository.save(memberJoinedFiveMonthsAgo());

        // when
        List<MemberRegistrationAware> members = memberService.getAllMembersSortedByRegistrationDate();
        Iterator<MemberRegistrationAware> membersIterator = members.iterator();

        // then
        assertThat(membersIterator.next().getDisplayedName().toString()).isEqualTo("John");
        assertThat(membersIterator.next().getDisplayedName().toString()).isEqualTo("Mark");
        assertThat(membersIterator.next().getDisplayedName().toString()).isEqualTo("Administrator");
        assertThat(membersIterator.next().getDisplayedName().toString()).isEqualTo("Tom");
        assertThat(membersIterator.hasNext()).isFalse();
    }

    @Test
    public void shouldReturnOnlyAdministratorAccount_whenThereIsNoMemberRegistered() throws Exception {
        // when
        List<MemberRegistrationAware> members = memberService.getAllMembersSortedByRegistrationDate();

        // then
        assertThat(members).hasSize(1);
        assertThat(members.get(0).getDisplayedName().toString()).isEqualTo("Administrator");
    }

    private MemberEntity memberJoinedFiveMonthsAgo() {
        return MemberEntity.builder()
                .username(Username.builder().value("john").build())
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
                .username(Username.builder().value("mark").build())
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
                .username(Username.builder().value("tom").build())
                .displayedName(DisplayedName.builder().value("Tom").build())
                .email(Email.builder().value("tom@tom.com").build())
                .registrationMetaData(RegistrationMetaDataEntity.builder()
                        .joinDateTime(LocalDateTime.now().plusWeeks(2))
                        .ipAddress(IPAddress.builder().value("127.0.0.1").build()
                        ).build())
                .build();
    }
}