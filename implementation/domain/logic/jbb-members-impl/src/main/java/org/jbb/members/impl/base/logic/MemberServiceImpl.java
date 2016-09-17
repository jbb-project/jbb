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


import com.google.common.collect.Sets;

import org.jbb.lib.core.vo.Username;
import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.Member;
import org.jbb.members.api.data.MemberRegistrationAware;
import org.jbb.members.api.exception.DisplayedNameException;
import org.jbb.members.api.service.MemberService;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
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

@Service
public class MemberServiceImpl implements MemberService {
    private final Validator validator;

    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(Validator validator,
                             MemberRepository memberRepository) {
        this.validator = validator;
        this.memberRepository = memberRepository;
    }

    @Override
    public List<MemberRegistrationAware> getAllMembersSortedByRegistrationDate() {
        return memberRepository.findAllByOrderByRegistrationDateAsc()
                .stream()
                .map(memberEntity -> (MemberRegistrationAware) memberEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Member> getMemberWithUsername(Username username) {
        Optional<MemberEntity> member = memberRepository.findByUsername(username);
        return Optional.ofNullable(member.orElse(null));
    }

    @Override
    @Transactional
    public void updateDisplayedName(Username username, DisplayedName newDisplayedName) {
        Optional<MemberEntity> member = memberRepository.findByUsername(username);
        if (member.isPresent()) {
            MemberEntity memberEntity = member.get();
            memberEntity.setDisplayedName(newDisplayedName);

            Set<ConstraintViolation<?>> validationResult = Sets.newHashSet();
            validationResult.addAll(validator.validate(memberEntity));

            if (!validationResult.isEmpty()) {
                throw new DisplayedNameException(validationResult);
            }

            memberRepository.save(memberEntity);
        } else {
            throw new UsernameNotFoundException(String.format("Member with username '%s' not found", username));
        }
    }

}
