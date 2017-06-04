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

import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.data.Member;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.data.MembersProperties;
import org.jbb.members.impl.base.model.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailNotBusyValidator implements ConstraintValidator<EmailNotBusy, MemberEntity> {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserDetailsSource userDetailsSource;

    @Autowired
    private MembersProperties properties;

    private String messageTemplate;


    @Override
    public void initialize(EmailNotBusy emailNotBusy) {
        messageTemplate = emailNotBusy.message();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(MemberEntity memberEntity, ConstraintValidatorContext constraintValidatorContext) {
        Email email = memberEntity.getEmail();
        Long counter = memberRepository.countByEmail(email);
        boolean result = properties.allowEmailDuplication() ||
                (counter == 0 || currentUserIsUsing(memberEntity));
        if (!result) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(messageTemplate)
                    .addPropertyNode("email").addConstraintViolation();
        }
        return result;
    }

    private boolean currentUserIsUsing(MemberEntity memberEntity) {
        UserDetails userDetails = userDetailsSource.getFromApplicationContext();
        if (userDetails != null) {
            Username currentUsername = Username.builder().value(userDetails.getUsername()).build();
            List<Member> membersWithEmail = memberRepository.findByEmail(memberEntity.getEmail());
            return memberEntity.getUsername().equals(currentUsername) && membersWithEmail.stream()
                    .anyMatch(member -> member.getUsername().equals(currentUsername));
        }
        return false;
    }
}
