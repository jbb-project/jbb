/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lock;

import org.jbb.lib.core.time.JBBTime;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CleanHsqlDbAfterTestsConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.security.api.service.UserLockService;
import org.jbb.security.impl.MemberConfigMocks;
import org.jbb.security.impl.SecurityConfig;
import org.jbb.security.impl.lock.dao.InvalidSignInAttemptRepository;
import org.jbb.security.impl.lock.dao.UserLockRepository;
import org.jbb.security.impl.lock.model.InvalidSignInAttemptEntity;
import org.jbb.security.impl.lock.model.UserLockEntity;
import org.jbb.security.impl.lock.properties.UserLockProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfigMocks.class, CleanHsqlDbAfterTestsConfig.class,
        SecurityConfig.class, PropertiesConfig.class,
        EventBusConfig.class, DbConfig.class, MemberConfigMocks.class})
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
public class UserLockServiceImplIT {

    @Autowired
    private InvalidSignInAttemptRepository invalidSignInAttemptRepository;

    @Autowired
    private UserLockRepository userLockRepository;

    @Autowired
    private UserLockService userLockService;

    @Autowired
    private UserLockProperties userLockProperties;

    @Autowired
    private JBBTime time;


    private Clock clock;

    @Before
    public void init() {
        LocalDateTime localDateTime = LocalDateTime.of(2016, 12, 12, 12, 00);
        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        JBBTime.setClock(clock);
    }

    @After
    public void tearDown() throws Exception {
        userLockRepository.deleteAll();
        invalidSignInAttemptRepository.deleteAll();
    }

    @Test
    public void whenServiceIsNotAvailableAndUserHasMoreInvalidAttemptThenPropertiesValue_UserShouldNotBeBlocked() {

        //given
        userLockProperties.setProperty(UserLockProperties.USER_LOCK_SERVICE_AVAILABLE, Boolean.FALSE.toString());

        //when
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);

        //then
        assertThat(userLockRepository.findByMemberID(1L)).isEmpty();

    }

    @Test
    public void whenUserHasMoreInvalidAttemptThenPropertiesValue_UserShouldBeBlocked() {

        //when
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);

        //then
        assertThat(userLockRepository.findByMemberID(1L)).isNotEmpty();
        Optional<UserLockEntity> byMemberID = userLockRepository.findByMemberID(1L);
        assertTrue(byMemberID.get().getMemberID().equals(1L));

    }

    @Test
    public void whenUserHasLessInvalidAttemptThenPropertiesValue_UserShouldNotBeBlocked() {

        //when
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);

        //then
        assertThat(userLockRepository.findByMemberID(1L)).isEmpty();
    }

    @Test
    public void whenUserHasLessInvalidAttemptsInPeriodOfTimeWhichIsLessThenPropertiesValue_UserShouldNoBeBlocked() {

        //given
        LocalDateTime localDateTime = LocalDateTime.of(2016, 12, 12, 12, 00);
        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        JBBTime.setClock(clock);
        userLockService.lockUserIfQualify(1L); //12.00

        localDateTime = LocalDateTime.of(2016, 12, 12, 12, 4);
        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        JBBTime.setClock(clock);
        userLockService.lockUserIfQualify(1L); //12.04

        localDateTime = LocalDateTime.of(2016, 12, 12, 12, 8);
        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        JBBTime.setClock(clock);
        userLockService.lockUserIfQualify(1L); //12.08

        //when

        //then
        assertThat(userLockRepository.findByMemberID(1L)).isEmpty();
    }

    @Test
    public void whenUserHasLessInvalidAttemptsInPeriodOfTimeWhichIsGreaterThenPropertiesValue_UserShouldNoBeBlocked() {


        //given
        LocalDateTime localDateTime = LocalDateTime.of(2016, 12, 12, 12, 00);
        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        JBBTime.setClock(clock);
        userLockService.lockUserIfQualify(1L); //12.00

        localDateTime = LocalDateTime.of(2016, 12, 12, 12, 4);
        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        JBBTime.setClock(clock);
        userLockService.lockUserIfQualify(1L); //12.04

        localDateTime = LocalDateTime.of(2016, 12, 12, 12, 15);
        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        JBBTime.setClock(clock);
        userLockService.lockUserIfQualify(1L); //12.15

        //when

        //then
        assertThat(userLockRepository.findByMemberID(1L)).isEmpty();
    }

    @Test
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void whenUserHasTooInvalidAttemptsAndUserNotExceedInvalidAttemptsValue_TooOldAttemptsShouldBeRemoved_UserShouldNotBeBlocked(){

        //given
        LocalDateTime localDateTime = LocalDateTime.of(2016, 12, 12, 12, 0);
        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        JBBTime.setClock(clock);
        userLockService.lockUserIfQualify(1L); //12.00

        localDateTime = LocalDateTime.of(2016, 12, 12, 12, 4);
        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        JBBTime.setClock(clock);
        userLockService.lockUserIfQualify(1L); //12.04

        localDateTime = LocalDateTime.of(2016, 12, 12, 12, 8);
        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        JBBTime.setClock(clock);
        userLockService.lockUserIfQualify(1L); //12.08

        localDateTime = LocalDateTime.of(2016, 12, 12, 12, 15);
        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        JBBTime.setClock(clock);
        userLockService.lockUserIfQualify(1L); //12.15

        //when

        //then
        assertThat(userLockRepository.findByMemberID(1L)).isEmpty();

        List<InvalidSignInAttemptEntity> allWithSpecifyMember = invalidSignInAttemptRepository.findAllWithSpecifyMember(1L);
        List<InvalidSignInAttemptEntity> result = allWithSpecifyMember.stream()
                .filter(invalidSignInAttemptEntity -> invalidSignInAttemptEntity.getInvalidAttemptDateTime().isEqual(LocalDateTime.of(2016, 12, 12, 12, 15))
                        || invalidSignInAttemptEntity.getInvalidAttemptDateTime().isEqual(LocalDateTime.of(2016, 12, 12, 12, 8)))
                .collect(Collectors.toList());

        assertThat(result.size()).isEqualTo(2);
    }
//
//    //given
//    LocalDateTime localDateTime = LocalDateTime.of(2016, 12, 12, 12, 0);
//        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
//        JBBTime.setClock(clock);
//        userLockService.lockUserIfQualify(1L); //12.00
//
//    localDateTime = LocalDateTime.of(2016, 12, 12, 12, 4);
//        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
//        JBBTime.setClock(clock);
//        userLockService.lockUserIfQualify(1L); //12.04
//
//    localDateTime = LocalDateTime.of(2016, 12, 12, 12, 8);
//        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
//        JBBTime.setClock(clock);
//        userLockService.lockUserIfQualify(1L); //12.08
//
//    localDateTime = LocalDateTime.of(2016, 12, 12, 12, 9);
//        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
//        JBBTime.setClock(clock);
//        userLockService.lockUserIfQualify(1L); //12.10
//
//    localDateTime = LocalDateTime.of(2016, 12, 12, 12, 13);
//        this.clock = Clock.fixed(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
//        JBBTime.setClock(clock);
//        userLockService.lockUserIfQualify(1L); //12.13
}
