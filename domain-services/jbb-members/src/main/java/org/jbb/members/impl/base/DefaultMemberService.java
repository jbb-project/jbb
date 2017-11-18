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


import com.google.common.collect.Sets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.commons.web.HttpRequestContext;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.members.api.base.AccountDataToChange;
import org.jbb.members.api.base.AccountException;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberSearchCriteria;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.base.ProfileDataToChange;
import org.jbb.members.api.base.ProfileException;
import org.jbb.members.api.registration.MemberRegistrationAware;
import org.jbb.members.event.MemberAccountChangedEvent;
import org.jbb.members.event.MemberProfileChangedEvent;
import org.jbb.members.event.MemberRemovedEvent;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.members.impl.base.search.MemberSpecificationCreator;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.api.password.PasswordService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMemberService implements MemberService {
    private final Validator validator;
    private final MemberRepository memberRepository;
    private final MemberSpecificationCreator specificationCreator;
    private final PasswordService passwordService;
    private final JbbEventBus eventBus;
    private final HttpRequestContext httpRequestContext;

    @Override
    public List<MemberRegistrationAware> getAllMembersSortedByRegistrationDate() {
        return memberRepository.findAllByOrderByRegistrationDateAsc()
                .stream()
                .map(memberEntity -> (MemberRegistrationAware) memberEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Member> getCurrentMember() {
        return httpRequestContext.getCurrentMemberId()
            .map(memberRepository::findOne);
    }

    @Override
    public Member getCurrentMemberChecked() throws MemberNotFoundException {
        return getCurrentMember().orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public Optional<Member> getMemberWithId(Long id) {
        Validate.notNull(id);
        return Optional.ofNullable(memberRepository.findOne(id));
    }

    @Override
    public Member getMemberWithIdChecked(Long id) throws MemberNotFoundException {
        return getMemberWithId(id).orElseThrow(() -> new MemberNotFoundException(id));
    }

    @Override
    public Optional<Member> getMemberWithUsername(Username username) {
        Optional<MemberEntity> member = memberRepository.findByUsername(username);
        return Optional.ofNullable(member.orElse(null));
    }

    @Override
    @Transactional
    public void updateProfile(Long memberId, ProfileDataToChange profileDataToChange) {
        Optional<DisplayedName> newDisplayedName = profileDataToChange.getDisplayedName();
        newDisplayedName.ifPresent(displayedName -> updateDisplayedName(memberId, displayedName));
        eventBus.post(new MemberProfileChangedEvent(memberId));
    }

    @Override
    @Transactional
    public void updateAccount(Long memberId, AccountDataToChange accountDataToChange) {
        Set<ConstraintViolation<?>> validationResult = Sets.newHashSet();

        Optional<Email> newEmail = accountDataToChange.getEmail();
        if (newEmail.isPresent()) {
            try {
                updateEmail(memberId, newEmail.get());
            } catch (AccountException e) {
                log.trace("Problem with updating email for member id {} with data to change: {}",
                        memberId, newEmail.get(), e);
                validationResult.addAll(e.getConstraintViolations());
            }
        }

        Optional<Password> newPassword = accountDataToChange.getNewPassword();
        if (newPassword.isPresent()) {
            try {
                passwordService.changeFor(memberId, newPassword.get());
            } catch (PasswordException e) {
                log.trace("Problem with updating password for member id {}", memberId, e);
                validationResult.addAll(e.getConstraintViolations());
            }
        }

        if (!validationResult.isEmpty()) {
            throw new AccountException(validationResult);
        }

        eventBus.post(new MemberAccountChangedEvent(memberId));
    }

    @Override
    public Page<MemberRegistrationAware> getAllMembersWithCriteria(MemberSearchCriteria criteria) {
        Validate.notNull(criteria);
        Page<MemberEntity> resultPage = memberRepository
            .findAll(specificationCreator.createSpecification(criteria), criteria.getPageRequest());
        return resultPage.map(memberEntity -> (MemberRegistrationAware) memberEntity);
    }

    @Override
    @Transactional
    public void removeMember(Long memberId) {
        Validate.notNull(memberId);
        memberRepository.delete(memberId);
        eventBus.post(new MemberRemovedEvent(memberId));
    }

    private void updateDisplayedName(Long memberId, DisplayedName newDisplayedName) {
        Optional<MemberEntity> member = Optional.ofNullable(memberRepository.findOne(memberId));
        if (member.isPresent()) {
            MemberEntity memberEntity = member.get();
            memberEntity.setDisplayedName(newDisplayedName);

            Set<ConstraintViolation<?>> validationResult = Sets.newHashSet();
            validationResult.addAll(validator.validate(memberEntity));

            if (!validationResult.isEmpty()) {
                throw new ProfileException(validationResult);
            }

            memberRepository.save(memberEntity);
        } else {
            throw new UsernameNotFoundException(String.format("Member with id '%d' not found", memberId));
        }
    }

    private void updateEmail(Long memberId, Email email) {
        Optional<MemberEntity> member = Optional.ofNullable(memberRepository.findOne(memberId));
        if (member.isPresent()) {
            MemberEntity memberEntity = member.get();
            memberEntity.setEmail(email);

            Set<ConstraintViolation<?>> validationResult = Sets.newHashSet();
            validationResult.addAll(validator.validate(memberEntity));

            if (!validationResult.isEmpty()) {
                throw new AccountException(validationResult);
            }

            memberRepository.save(memberEntity);
        } else {
            throw new UsernameNotFoundException(String.format("Member with id '%d' not found", memberId));
        }
    }

}
