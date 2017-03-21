/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.logic;

import org.jbb.lib.core.CoreConfig;
import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.IPAddress;
import org.jbb.lib.core.vo.Username;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CleanH2DbAfterTestsConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.lib.test.SpringSecurityConfigMocks;
import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.MemberRegistrationAware;
import org.jbb.members.api.data.MemberSearchCriteria;
import org.jbb.members.api.service.MemberService;
import org.jbb.members.impl.MembersConfig;
import org.jbb.members.impl.SecurityConfigMocks;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.members.impl.registration.model.RegistrationMetaDataEntity;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CoreConfig.class, CoreConfigMocks.class, CleanH2DbAfterTestsConfig.class, SecurityConfigMocks.class,
        MembersConfig.class, PropertiesConfig.class,
        EventBusConfig.class, DbConfig.class, SpringSecurityConfigMocks.class})
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class MemberServiceSearchIT {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository repository;

    @After
    public void tearDown() throws Exception {
        repository.deleteAll();
    }

    @Test
    public void shouldReturnAll_whenCriteriaAreEmpty() throws Exception {
        // given
        MemberEntity memberEntity = repository.save(exampleMember("jack", LocalDateTime.now()));

        // when
        List<MemberRegistrationAware> results =
                memberService.getAllMembersWithCriteria(new TestbedMemberSearchCriteria());

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getId()).isEqualTo(memberEntity.getId());
    }

    @Test
    public void shouldReturnAll_withLetter_F_inUsername() throws Exception {
        // given
        repository.save(exampleMember("fan", LocalDateTime.now()));
        repository.save(exampleMember("stuff", LocalDateTime.now()));
        repository.save(exampleMember("john", LocalDateTime.now()));
        repository.save(exampleMember("koffing", LocalDateTime.now()));

        TestbedMemberSearchCriteria criteria = new TestbedMemberSearchCriteria();
        criteria.setUsername(Username.builder().value("f").build());

        // when
        List<MemberRegistrationAware> results =
                memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).hasSize(3);
        assertThat(results.get(0).getUsername().toString()).isEqualTo("fan");
        assertThat(results.get(1).getUsername().toString()).isEqualTo("stuff");
        assertThat(results.get(2).getUsername().toString()).isEqualTo("koffing");

        // when
        criteria.setSortBy(MemberSearchCriteria.SortColumn.USERNAME, MemberSearchCriteria.SortingOrder.DESC);
        results = memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).hasSize(3);
        assertThat(results.get(0).getUsername().toString()).isEqualTo("stuff");
        assertThat(results.get(1).getUsername().toString()).isEqualTo("koffing");
        assertThat(results.get(2).getUsername().toString()).isEqualTo("fan");
    }

    @Test
    public void shouldReturnAll_accordingToJoinTime() throws Exception {
        // given
        repository.save(exampleMember("fan", LocalDateTime.now().minusDays(5)));
        repository.save(exampleMember("stuff", LocalDateTime.now().minusDays(4)));
        repository.save(exampleMember("john", LocalDateTime.now().minusDays(3)));
        repository.save(exampleMember("koffing", LocalDateTime.now().minusDays(2)));

        TestbedMemberSearchCriteria criteria = new TestbedMemberSearchCriteria();
        criteria.setJoinCriteria(LocalDate.now().minusDays(2), MemberSearchCriteria.JoinMoment.THAT_DAY);

        // when
        List<MemberRegistrationAware> results =
                memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getUsername().toString()).isEqualTo("koffing");

        // when
        criteria.setJoinCriteria(LocalDate.now().minusDays(2), MemberSearchCriteria.JoinMoment.BEFORE);
        results = memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).hasSize(3);
        assertThat(results.get(0).getUsername().toString()).isEqualTo("fan");
        assertThat(results.get(1).getUsername().toString()).isEqualTo("stuff");
        assertThat(results.get(2).getUsername().toString()).isEqualTo("john");

        // when
        criteria.setJoinCriteria(LocalDate.now().minusDays(2), MemberSearchCriteria.JoinMoment.AFTER);
        results = memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).isEmpty();
    }

    @Test
    public void shouldReturnAll_sortedAccordingToSortBy() throws Exception {
        // given
        repository.save(exampleMember("anna", "Anna", "anna@gmail.com", LocalDateTime.now().minusDays(5)));
        repository.save(exampleMember("bart", "NSN", "bart@nsn.com", LocalDateTime.now().minusDays(4)));
        repository.save(exampleMember("tom", "Tom", "tom@tom.com", LocalDateTime.now().minusDays(3)));
        repository.save(exampleMember("cindy", "Cindy1", "cindy@gmail.com", LocalDateTime.now().minusDays(2)));

        TestbedMemberSearchCriteria criteria = new TestbedMemberSearchCriteria();
        criteria.setSortBy(MemberSearchCriteria.SortColumn.DISPLAYED_NAME, MemberSearchCriteria.SortingOrder.ASC);

        // when
        List<MemberRegistrationAware> results = memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).hasSize(4);
        assertThat(results.get(0).getUsername().toString()).isEqualTo("anna");
        assertThat(results.get(1).getUsername().toString()).isEqualTo("cindy");
        assertThat(results.get(2).getUsername().toString()).isEqualTo("bart");
        assertThat(results.get(3).getUsername().toString()).isEqualTo("tom");

        // when
        criteria.setSortBy(MemberSearchCriteria.SortColumn.EMAIL, MemberSearchCriteria.SortingOrder.ASC);
        results = memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).hasSize(4);
        assertThat(results.get(0).getUsername().toString()).isEqualTo("anna");
        assertThat(results.get(1).getUsername().toString()).isEqualTo("bart");
        assertThat(results.get(2).getUsername().toString()).isEqualTo("cindy");
        assertThat(results.get(3).getUsername().toString()).isEqualTo("tom");

        // when
        criteria.setSortBy(MemberSearchCriteria.SortColumn.JOIN_DATE, MemberSearchCriteria.SortingOrder.ASC);
        results = memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).hasSize(4);
        assertThat(results.get(0).getUsername().toString()).isEqualTo("anna");
        assertThat(results.get(1).getUsername().toString()).isEqualTo("bart");
        assertThat(results.get(2).getUsername().toString()).isEqualTo("tom");
        assertThat(results.get(3).getUsername().toString()).isEqualTo("cindy");

        // when
        criteria.setSortBy(MemberSearchCriteria.SortColumn.JOIN_DATE, MemberSearchCriteria.SortingOrder.DESC);
        criteria.setEmail(Email.builder().value("@gmail.com").build());
        criteria.setDisplayedName(DisplayedName.builder().value("n").build());
        results = memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getUsername().toString()).isEqualTo("cindy");
        assertThat(results.get(1).getUsername().toString()).isEqualTo("anna");
    }

    private MemberEntity exampleMember(String username, LocalDateTime joinTime) {
        return MemberEntity.builder()
                .username(Username.builder().value(username).build())
                .displayedName(DisplayedName.builder().value(username.toUpperCase()).build())
                .email(Email.builder().value(username + "@" + username + ".com").build())
                .registrationMetaData(RegistrationMetaDataEntity.builder()
                        .joinDateTime(joinTime)
                        .ipAddress(IPAddress.builder().value("127.0.0.1").build()
                        ).build())
                .build();
    }

    private MemberEntity exampleMember(String username, String displayedName,
                                       String email, LocalDateTime joinTime) {
        return MemberEntity.builder()
                .username(Username.builder().value(username).build())
                .displayedName(DisplayedName.builder().value(displayedName).build())
                .email(Email.builder().value(email).build())
                .registrationMetaData(RegistrationMetaDataEntity.builder()
                        .joinDateTime(joinTime)
                        .ipAddress(IPAddress.builder().value("127.0.0.1").build()
                        ).build())
                .build();
    }
}
