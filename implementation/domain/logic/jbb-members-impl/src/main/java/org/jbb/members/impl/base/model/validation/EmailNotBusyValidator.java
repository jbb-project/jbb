/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.model.validation;

import org.jbb.lib.core.security.UserDetailsSource;
import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.Username;
import org.jbb.members.api.data.Member;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.data.MembersProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailNotBusyValidator implements ConstraintValidator<EmailNotBusy, Email> {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserDetailsSource userDetailsSource;

    @Autowired
    private MembersProperties properties;


    @Override
    public void initialize(EmailNotBusy emailNotBusy) {
        // not needed
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(Email email, ConstraintValidatorContext constraintValidatorContext) {
        Long counter = memberRepository.countByEmail(email);
        return properties.allowEmailDuplication() ||
                (counter == 0 || currentUserIsUsing(email));
    }

    private boolean currentUserIsUsing(Email email) {
        UserDetails userDetails = userDetailsSource.getFromApplicationContext();
        if (userDetails != null) {
            Username currentUsername = Username.builder().value(userDetails.getUsername()).build();
            List<Member> membersWithEmail = memberRepository.findByEmail(email);
            return membersWithEmail.stream()
                    .anyMatch(member -> member.getUsername().equals(currentUsername));
        }
        return false;
    }
}
