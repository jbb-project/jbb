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


import com.google.common.collect.Sets;

import org.apache.commons.lang3.Validate;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.members.api.base.AccountDataToChange;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.registration.MemberRegistrationAware;
import org.jbb.members.api.base.MemberSearchCriteria;
import org.jbb.members.api.base.ProfileDataToChange;
import org.jbb.members.api.base.AccountException;
import org.jbb.members.api.base.ProfileException;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.event.MemberRemovedEvent;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.logic.search.MemberSpecificationCreator;
import org.jbb.members.impl.base.logic.search.SortCreator;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.api.password.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final Validator validator;
    private final MemberRepository memberRepository;
    private final MemberSpecificationCreator specificationCreator;
    private final SortCreator sortCreator;
    private final PasswordService passwordService;
    private final JbbEventBus eventBus;

    @Autowired
    public MemberServiceImpl(Validator validator,
                             MemberRepository memberRepository,
                             MemberSpecificationCreator specificationCreator,
                             SortCreator sortCreator,
                             PasswordService passwordService,
                             JbbEventBus eventBus) {
        this.validator = validator;
        this.memberRepository = memberRepository;
        this.specificationCreator = specificationCreator;
        this.sortCreator = sortCreator;
        this.passwordService = passwordService;
        this.eventBus = eventBus;
    }

    @Override
    public List<MemberRegistrationAware> getAllMembersSortedByRegistrationDate() {
        return memberRepository.findAllByOrderByRegistrationDateAsc()
                .stream()
                .map(memberEntity -> (MemberRegistrationAware) memberEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Member> getMemberWithId(Long id) {
        Validate.notNull(id);
        return Optional.ofNullable(memberRepository.findOne(id));
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
        if (newDisplayedName.isPresent()) {
            updateDisplayedName(memberId, newDisplayedName.get());
        }
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
    }

    @Override
    public List<MemberRegistrationAware> getAllMembersWithCriteria(MemberSearchCriteria criteria) {
        Validate.notNull(criteria);
        return memberRepository.findAll(specificationCreator.createSpecification(criteria), sortCreator.create(criteria))
                .stream()
                .map(memberEntity -> (MemberRegistrationAware) memberEntity)
                .collect(Collectors.toList());
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
