/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lockout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.members.event.MemberRemovedEvent;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.impl.BaseIT;
import org.jbb.security.impl.lockout.dao.FailedSignInAttemptRepository;
import org.jbb.security.impl.lockout.dao.MemberLockRepository;
import org.jbb.security.impl.lockout.model.FailedSignInAttemptEntity;
import org.jbb.security.impl.lockout.model.MemberLockEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class MemberLockoutServiceIT extends BaseIT {

    @Autowired
    private FailedSignInAttemptRepository failedSignInAttemptRepository;

    @Autowired
    private MemberLockRepository memberLockRepository;

    @Autowired
    private MemberLockoutService memberLockoutService;

    @Autowired
    private MemberLockProperties memberLockProperties;

    @Autowired
    private JbbEventBus eventBus;

    @Before
    public void init() {
        setCurrentTime(2016, 12, 12, 12, 00);
        setPropertiesToDefault();
    }

    @After
    public void tearDown() throws Exception {
        memberLockRepository.deleteAll();
        failedSignInAttemptRepository.deleteAll();
    }

    @Test
    public void whenLockoutIsDisabled_andMemberHasMoreFailedAttemptThanPropertiesValue_MemberShouldNotBeLocked() {

        //given
        memberLockProperties.setProperty(MemberLockProperties.MEMBER_LOCKOUT_ENABLED, Boolean.FALSE.toString());

        //when
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);

        //then
        assertThat(memberLockRepository.findByMemberId(1L)).isEmpty();

    }

    @Test
    public void whenLockoutIsEnabled_andMemberHasMoreFailedAttemptThanPropertiesValue_MemberShouldBeLocked() {

        //when
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);

        //then
        assertThat(memberLockRepository.findByMemberId(1L)).isNotEmpty();
        Optional<MemberLockEntity> byMemberID = memberLockRepository.findByMemberId(1L);
        assertTrue(byMemberID.get().getMemberId().equals(1L));

    }

    @Test
    public void whenMemberHasLessFailedAttemptsThanPropertiesValue_MemberShouldNotBeLocked() {

        //when
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);

        //then
        assertThat(memberLockRepository.findByMemberId(1L)).isEmpty();
    }

    @Test
    public void whenMemberHasLessFailedAttemptsInPeriodOfTimeWhichIsLessThanPropertiesValue_MemberShouldNoBeLocked() {

        //when
        setCurrentTime(2016, 12, 12, 12, 0);
        memberLockoutService.lockMemberIfQualify(1L); //12.00

        setCurrentTime(2016, 12, 12, 12, 4);
        memberLockoutService.lockMemberIfQualify(1L); //12.04

        setCurrentTime(2016, 12, 12, 12, 8);
        memberLockoutService.lockMemberIfQualify(1L); //12.08

        //then
        assertThat(memberLockRepository.findByMemberId(1L)).isEmpty();
    }

    @Test
    public void whenMemberHasLessFailedAttemptsInPeriodOfTimeWhichIsGreaterThanPropertiesValue_MemberShouldNoBeLocked() {

        //when
        setCurrentTime(2016, 12, 12, 12, 0);
        memberLockoutService.lockMemberIfQualify(1L); //12.00

        setCurrentTime(2016, 12, 12, 12, 4);
        memberLockoutService.lockMemberIfQualify(1L); //12.04

        setCurrentTime(2016, 12, 12, 12, 15);
        memberLockoutService.lockMemberIfQualify(1L); //12.15

        //then
        assertThat(memberLockRepository.findByMemberId(1L)).isEmpty();
    }

    @Test
    public void whenMemberHasTooOldFailedAttemptsAndMemberNotExceedFailedAttemptsValue_TooOldAttemptsShouldBeRemoved_MemberShouldNotBeLocked() {

        //when
        setCurrentTime(2016, 12, 12, 12, 0);
        memberLockoutService.lockMemberIfQualify(1L); //12.00

        setCurrentTime(2016, 12, 12, 12, 4);
        memberLockoutService.lockMemberIfQualify(1L); //12.04

        setCurrentTime(2016, 12, 12, 12, 8);
        memberLockoutService.lockMemberIfQualify(1L); //12.08

        setCurrentTime(2016, 12, 12, 12, 15);
        memberLockoutService.lockMemberIfQualify(1L); //12.15

        //when

        //then
        assertThat(memberLockRepository.findByMemberId(1L)).isEmpty();

        List<FailedSignInAttemptEntity> allWithSpecifyMember = failedSignInAttemptRepository.findAllForMember(1L);
        List<FailedSignInAttemptEntity> result = allWithSpecifyMember.stream()
                .filter(failedSignInAttemptEntity -> failedSignInAttemptEntity.getAttemptDateTime().isEqual(LocalDateTime.of(2016, 12, 12, 12, 15))
                        || failedSignInAttemptEntity.getAttemptDateTime().isEqual(LocalDateTime.of(2016, 12, 12, 12, 8)))
                .collect(Collectors.toList());

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void whenMemberHasAccountLockNewFailedsAttemptsAreNotSaveToDB() {

        //when
        setCurrentTime(2016, 12, 12, 12, 0);
        memberLockoutService.lockMemberIfQualify(1L); //12.00

        setCurrentTime(2016, 12, 12, 12, 4);
        memberLockoutService.lockMemberIfQualify(1L); //12.04

        setCurrentTime(2016, 12, 12, 12, 5);
        memberLockoutService.lockMemberIfQualify(1L); //12.05

        setCurrentTime(2016, 12, 12, 12, 10);
        memberLockoutService.lockMemberIfQualify(1L); //12.10

        setCurrentTime(2016, 12, 12, 12, 15);
        memberLockoutService.lockMemberIfQualify(1L); //12.15

        //then
        assertThat(memberLockRepository.findByMemberId(1L)).isNotEmpty();
        assertThat(failedSignInAttemptRepository.findAllForMember(1L)).isEmpty();
    }

    @Test
    public void whenMemberLockIsEnded_LockIsRemovedFromDB() {

        //given
        setCurrentTime(2016, 12, 12, 12, 0);
        memberLockoutService.lockMemberIfQualify(1L); //12.00

        setCurrentTime(2016, 12, 12, 12, 4);
        memberLockoutService.lockMemberIfQualify(1L); //12.04

        setCurrentTime(2016, 12, 12, 12, 5);
        memberLockoutService.lockMemberIfQualify(1L); //12.05

        setCurrentTime(2016, 12, 12, 12, 10);
        memberLockoutService.lockMemberIfQualify(1L); //12.10


        //when
        setCurrentTime(2016, 12, 12, 12, 19);
        boolean userHasLock = memberLockoutService.isMemberHasLock(1L);

        setCurrentTime(2016, 12, 12, 12, 21);
        boolean userHasLockAfterExpirationDate = memberLockoutService.isMemberHasLock(1L);


        //then
        assertTrue(userHasLock);
        assertFalse(userHasLockAfterExpirationDate);
    }

    @Test
    public void deleteAllFailedsAttemptsForMember() {

        //given
        saveFailedSignInAttemptForMember(1L);
        saveFailedSignInAttemptForMember(1L);
        saveFailedSignInAttemptForMember(2L);
        saveFailedSignInAttemptForMember(3L);

        //when
        memberLockoutService.cleanFailedAttemptsForMember(1L);

        //then
        assertThat(failedSignInAttemptRepository.findAllForMember(1L)).isEmpty();
        assertThat(failedSignInAttemptRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    public void shouldRemoveFailedAttempts_andLock_whenMemberRemovedEventReceived() throws Exception {
        // given
        Long memberId = 100L;

        saveFailedSignInAttemptForMember(memberId);
        saveFailedSignInAttemptForMember(memberId);
        saveFailedSignInAttemptForMember(memberId);
        saveFailedSignInAttemptForMember(memberId);
        saveLockForMember(memberId);

        // when
        eventBus.post(new MemberRemovedEvent(memberId));

        // then
        assertThat(failedSignInAttemptRepository.findAllForMember(memberId)).isEmpty();
        assertThat(memberLockRepository.findByMemberId(memberId)).isNotPresent();
    }

    @After
    public void restoreDefaultDateTimeProvider() {
        DateTimeProvider.setDefault();
    }

    private void saveFailedSignInAttemptForMember(Long memberId) {
        failedSignInAttemptRepository.save(FailedSignInAttemptEntity.builder().memberId(memberId)
                .attemptDateTime(LocalDateTime.now())
                .build()
        );
    }

    private void saveLockForMember(Long memberId) {
        memberLockRepository.save(MemberLockEntity.builder()
                .memberId(memberId).expirationDate(LocalDateTime.now()).build());
    }

    private void setPropertiesToDefault() {
        memberLockProperties.setProperty(MemberLockProperties.MEMBER_LOCKOUT_ENABLED, Boolean.TRUE.toString());
        memberLockProperties.setProperty(MemberLockProperties.MEMBER_LOCKOUT_DURATION_MINUTES, "10");
        memberLockProperties.setProperty(MemberLockProperties.MEMBER_LOCKOUT_ATTEMPTS_EXPIRATION, "10");
        memberLockProperties.setProperty(MemberLockProperties.MEMBER_LOCKOUT_ATTEMPTS_THRESHOLD, "4");
    }

    private void setCurrentTime(int year, int month, int day, int hour, int minute) {
        LocalDateTime fixedNowDateTime = LocalDateTime.of(year, month, day, hour, minute);
        Clock clock = Clock.fixed(fixedNowDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        DateTimeProvider.setClock(clock);
    }
}
