/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base;

import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.MemberSearchCriteria;
import org.jbb.members.api.base.MemberSearchCriteria.JoinCriteria;
import org.jbb.members.api.base.MemberSearchCriteria.JoinMoment;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.registration.MemberRegistrationAware;
import org.jbb.members.impl.BaseIT;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.members.impl.registration.model.RegistrationMetaDataEntity;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberServiceSearchIT extends BaseIT {
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
        Page<MemberRegistrationAware> results =
                memberService.getAllMembersWithCriteria(new MemberSearchCriteria());

        // then
        assertThat(results).hasSize(1);
        assertThat(results.getContent().get(0).getId()).isEqualTo(memberEntity.getId());
    }

    @Test
    public void shouldReturnAll_withLetter_F_inUsername() throws Exception {
        // given
        repository.save(exampleMember("fan", LocalDateTime.now()));
        repository.save(exampleMember("stuff", LocalDateTime.now()));
        repository.save(exampleMember("john", LocalDateTime.now()));
        repository.save(exampleMember("koffing", LocalDateTime.now()));

        MemberSearchCriteria criteria = new MemberSearchCriteria();
        criteria.setUsername(Username.builder().value("f").build());

        // when
        Page<MemberRegistrationAware> results =
                memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).hasSize(3);
        assertThat(results.getContent().get(0).getUsername().toString()).isEqualTo("fan");
        assertThat(results.getContent().get(1).getUsername().toString()).isEqualTo("stuff");
        assertThat(results.getContent().get(2).getUsername().toString()).isEqualTo("koffing");

        // when
        criteria.setPageRequest(new PageRequest(0, 20, Direction.DESC, "username"));
        results = memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).hasSize(3);
        assertThat(results.getContent().get(0).getUsername().toString()).isEqualTo("stuff");
        assertThat(results.getContent().get(1).getUsername().toString()).isEqualTo("koffing");
        assertThat(results.getContent().get(2).getUsername().toString()).isEqualTo("fan");
    }

    @Test
    public void shouldReturnAll_accordingToJoinTime() throws Exception {
        // given
        repository.save(exampleMember("fan", LocalDateTime.now().minusDays(5)));
        repository.save(exampleMember("stuff", LocalDateTime.now().minusDays(4)));
        repository.save(exampleMember("john", LocalDateTime.now().minusDays(3)));
        repository.save(exampleMember("koffing", LocalDateTime.now().minusDays(2)));

        MemberSearchCriteria criteria = new MemberSearchCriteria();
        criteria.setJoinCriteria(
                JoinCriteria.builder()
                        .joinDate(LocalDate.now().minusDays(2))
                        .joinMoment(JoinMoment.THAT_DAY)
                        .build()
        );

        // when
        Page<MemberRegistrationAware> results =
                memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).hasSize(1);
        assertThat(results.getContent().get(0).getUsername().toString()).isEqualTo("koffing");

        // when
        criteria.setJoinCriteria(
                JoinCriteria.builder()
                        .joinDate(LocalDate.now().minusDays(2))
                        .joinMoment(JoinMoment.BEFORE)
                        .build()
        );
        results = memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).hasSize(3);
        assertThat(results.getContent().get(0).getUsername().toString()).isEqualTo("fan");
        assertThat(results.getContent().get(1).getUsername().toString()).isEqualTo("stuff");
        assertThat(results.getContent().get(2).getUsername().toString()).isEqualTo("john");

        // when
        criteria.setJoinCriteria(
                JoinCriteria.builder()
                        .joinDate(LocalDate.now().minusDays(2))
                        .joinMoment(JoinMoment.AFTER)
                        .build()
        );
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

        MemberSearchCriteria criteria = new MemberSearchCriteria();
        criteria.setPageRequest(new PageRequest(0, 20, Direction.ASC, "displayedName"));

        // when
        Page<MemberRegistrationAware> results = memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).hasSize(4);
        assertThat(results.getContent().get(0).getUsername().toString()).isEqualTo("anna");
        assertThat(results.getContent().get(1).getUsername().toString()).isEqualTo("cindy");
        assertThat(results.getContent().get(2).getUsername().toString()).isEqualTo("bart");
        assertThat(results.getContent().get(3).getUsername().toString()).isEqualTo("tom");

        // when
        criteria.setPageRequest(new PageRequest(0, 20, Direction.ASC, "email"));
        results = memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).hasSize(4);
        assertThat(results.getContent().get(0).getUsername().toString()).isEqualTo("anna");
        assertThat(results.getContent().get(1).getUsername().toString()).isEqualTo("bart");
        assertThat(results.getContent().get(2).getUsername().toString()).isEqualTo("cindy");
        assertThat(results.getContent().get(3).getUsername().toString()).isEqualTo("tom");

        // when
        criteria.setPageRequest(
                new PageRequest(0, 20, Direction.ASC, "registrationMetaData.joinDateTime"));
        results = memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).hasSize(4);
        assertThat(results.getContent().get(0).getUsername().toString()).isEqualTo("anna");
        assertThat(results.getContent().get(1).getUsername().toString()).isEqualTo("bart");
        assertThat(results.getContent().get(2).getUsername().toString()).isEqualTo("tom");
        assertThat(results.getContent().get(3).getUsername().toString()).isEqualTo("cindy");

        // when
        criteria.setPageRequest(
                new PageRequest(0, 20, Direction.DESC, "registrationMetaData.joinDateTime"));
        criteria.setEmail(Email.builder().value("@gmail.com").build());
        criteria.setDisplayedName(DisplayedName.builder().value("n").build());
        results = memberService.getAllMembersWithCriteria(criteria);

        // then
        assertThat(results).hasSize(2);
        assertThat(results.getContent().get(0).getUsername().toString()).isEqualTo("cindy");
        assertThat(results.getContent().get(1).getUsername().toString()).isEqualTo("anna");
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
