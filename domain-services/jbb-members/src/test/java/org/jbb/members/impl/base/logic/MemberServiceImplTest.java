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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.members.api.base.AccountDataToChange;
import org.jbb.members.api.base.AccountException;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.ProfileDataToChange;
import org.jbb.members.api.base.ProfileException;
import org.jbb.members.api.registration.MemberRegistrationAware;
import org.jbb.members.event.MemberRemovedEvent;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.logic.search.MemberSpecificationCreator;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.api.password.PasswordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class MemberServiceImplTest {

    @Mock
    private Validator validatorMock;

    @Mock
    private MemberRepository memberRepositoryMock;

    @Mock
    private PasswordService passwordServiceMock;

    @Mock
    private MemberSpecificationCreator specificationCreatorMock;

    @Mock
    private JbbEventBus eventBusMock;

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

    @Test
    public void shouldNotInteractWithRepository_whenUpdateProfileInvoked_andDisplayedNameIsAbsent() throws Exception {
        // given
        ProfileDataToChange profileDataToChange = mock(ProfileDataToChange.class);
        given(profileDataToChange.getDisplayedName()).willReturn(Optional.empty());

        // when
        memberService.updateProfile(1L, profileDataToChange);

        // then
        verifyZeroInteractions(memberRepositoryMock);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowUserNotFoundException_duringUpdateProfile_forNotExistUser() throws Exception {
        // given
        ProfileDataToChange profileDataToChange = mock(ProfileDataToChange.class);
        given(profileDataToChange.getDisplayedName()).willReturn(Optional.of(DisplayedName.builder().build()));
        given(memberRepositoryMock.findOne(any(Long.class))).willReturn(null);

        // when
        memberService.updateProfile(1L, profileDataToChange);

        // then
        // throw UserNotFoundException
    }

    @Test
    public void shouldSaveMember_duringUpdateProfile_forUser() throws Exception {
        // given
        ProfileDataToChange profileDataToChange = mock(ProfileDataToChange.class);
        given(profileDataToChange.getDisplayedName()).willReturn(Optional.of(DisplayedName.builder().build()));
        given(memberRepositoryMock.findOne(any(Long.class))).willReturn(mock(MemberEntity.class));
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet());

        // when
        memberService.updateProfile(1L, profileDataToChange);

        // then
        verify(memberRepositoryMock, times(1)).save(any(MemberEntity.class));
    }

    @Test(expected = ProfileException.class)
    public void shouldThrowProfileException_duringUpdateProfile_forUser_whenValidationErrorOccured() throws Exception {
        // given
        ProfileDataToChange profileDataToChange = mock(ProfileDataToChange.class);
        given(profileDataToChange.getDisplayedName()).willReturn(Optional.of(DisplayedName.builder().build()));
        given(memberRepositoryMock.findOne(any(Long.class))).willReturn(mock(MemberEntity.class));
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        memberService.updateProfile(1L, profileDataToChange);

        // then
        // throw ProfileException
    }

    @Test
    public void shouldNotInteractWithRepository_whenUpdateAccountInvoked_andAccountDataAreAbsent() throws Exception {
        // given
        Long anyId = 3L;
        AccountDataToChange accountDataToChange = mock(AccountDataToChange.class);
        given(accountDataToChange.getEmail()).willReturn(Optional.empty());
        given(accountDataToChange.getNewPassword()).willReturn(Optional.empty());

        // when
        memberService.updateAccount(anyId, accountDataToChange);

        // then
        verifyZeroInteractions(memberRepositoryMock);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowUserNotFoundException_duringUpdateAccount_forNotExistUser() throws Exception {
        // given
        Long anyId = 3L;
        AccountDataToChange accountDataToChange = mock(AccountDataToChange.class);
        given(accountDataToChange.getEmail()).willReturn(Optional.of(Email.builder().build()));

        // when
        memberService.updateAccount(anyId, accountDataToChange);

        // then
        // throw UserNotFoundException
    }

    @Test
    public void shouldSaveMember_duringUpdateAccount_forUser() throws Exception {
        // given
        Long anyId = 3L;
        AccountDataToChange accountDataToChange = mock(AccountDataToChange.class);
        given(accountDataToChange.getEmail()).willReturn(Optional.of(Email.builder().build()));
        given(accountDataToChange.getNewPassword()).willReturn(Optional.empty());
        given(memberRepositoryMock.findOne(any(Long.class))).willReturn(mock(MemberEntity.class));
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet());

        // when
        memberService.updateAccount(anyId, accountDataToChange);

        // then
        verify(memberRepositoryMock, times(1)).save(any(MemberEntity.class));
    }

    @Test(expected = AccountException.class)
    public void shouldThrowAccountException_duringUpdateProfile_forUser_whenValidationErrorOccured() throws Exception {
        // given
        Long anyId = 3L;
        AccountDataToChange accountDataToChange = mock(AccountDataToChange.class);
        given(accountDataToChange.getEmail()).willReturn(Optional.of(Email.builder().build()));
        given(accountDataToChange.getNewPassword()).willReturn(Optional.empty());
        given(memberRepositoryMock.findOne(any(Long.class))).willReturn(mock(MemberEntity.class));
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        memberService.updateAccount(anyId, accountDataToChange);

        // then
        // throw AccountException
    }

    @Test
    public void shouldSaveNewPassword_whenUpdateAccountInvoked() throws Exception {
        // given
        Long anyId = 3L;
        AccountDataToChange accountDataToChange = mock(AccountDataToChange.class);
        given(accountDataToChange.getEmail()).willReturn(Optional.of(Email.builder().build()));
        given(accountDataToChange.getNewPassword()).willReturn(Optional.of(Password.builder().build()));
        given(memberRepositoryMock.findOne(any(Long.class))).willReturn(mock(MemberEntity.class));
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet());

        // when
        memberService.updateAccount(anyId, accountDataToChange);

        // then
        verify(passwordServiceMock, times(1)).changeFor(any(Long.class), any(Password.class));
    }

    @Test(expected = AccountException.class)
    public void shouldThrowAccountException_whenUpdateAccountInvoked_andSomethingIsWrongWithNewPassword() throws Exception {
        // given
        Long anyId = 3L;
        AccountDataToChange accountDataToChange = mock(AccountDataToChange.class);
        given(accountDataToChange.getEmail()).willReturn(Optional.of(Email.builder().build()));
        given(accountDataToChange.getNewPassword()).willReturn(Optional.of(Password.builder().build()));
        given(memberRepositoryMock.findOne(any(Long.class))).willReturn(mock(MemberEntity.class));
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet());

        PasswordException passwordException = mock(PasswordException.class);
        given(passwordException.getConstraintViolations()).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));
        Mockito.doThrow(passwordException).when(passwordServiceMock).changeFor(any(Long.class), any(Password.class));

        // when
        memberService.updateAccount(anyId, accountDataToChange);

        // then
        // throw AccountException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullIdPassed() throws Exception {
        // when
        memberService.getMemberWithId(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldReturnEmptyOptional_whenMemberNotFound() throws Exception {
        // given
        given(memberRepositoryMock.findOne(any(Long.class))).willReturn(null);

        // when
        Optional<Member> member = memberService.getMemberWithId(11L);

        // then
        assertThat(member).isEmpty();
    }

    @Test
    public void shouldReturnMemberById() throws Exception {
        // given
        MemberEntity memberMock = mock(MemberEntity.class);
        given(memberRepositoryMock.findOne(any(Long.class))).willReturn(memberMock);

        // when
        Optional<Member> member = memberService.getMemberWithId(11L);

        // then
        assertThat(member.get()).isEqualTo(memberMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullCriteriaPassed() throws Exception {
        // when
        memberService.getAllMembersWithCriteria(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullIdPassed_whenRemoving() throws Exception {
        // when
        memberService.removeMember(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldRemoveMemberUsingRepository_whenRemovingMethodInvoked() throws Exception {
        // given
        Long id = 12L;

        // when
        memberService.removeMember(id);

        // then
        verify(memberRepositoryMock, times(1)).delete(eq(id));
    }

    @Test
    public void shouldEmitMemberRemovedEvent_whenRemovingMethodInvoked() throws Exception {
        // given
        Long id = 12L;

        // when
        memberService.removeMember(id);

        // then
        verify(eventBusMock).post(any(MemberRemovedEvent.class));
    }
}