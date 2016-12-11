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

import org.jbb.lib.core.security.SecurityContentUser;
import org.jbb.lib.core.security.UserDetailsSource;
import org.jbb.lib.core.vo.Username;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.security.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class UsernameNotBusyValidator implements ConstraintValidator<UsernameNotBusy, MemberEntity> {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserDetailsSource userDetailsSource;

    @Autowired
    private RoleService roleService;

    private String messageTemplate;

    @Override
    public void initialize(UsernameNotBusy usernameNotBusy) {
        messageTemplate = usernameNotBusy.message();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(MemberEntity memberEntity, ConstraintValidatorContext constraintValidatorContext) {
        Username username = memberEntity.getUsername();
        Long counter = memberRepository.countByUsername(username);
        boolean result = counter == 0 || (counter == 1 && (currentUserIsUsing(username) || callerIsAnAdministrator()));

        if (!result) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(messageTemplate)
                    .addPropertyNode("username").addConstraintViolation();
        }
        return result;
    }

    private boolean currentUserIsUsing(Username username) {
        UserDetails userDetails = userDetailsSource.getFromApplicationContext();
        if (userDetails != null) {
            return userDetails.getUsername().equals(username.getValue());
        } else {
            return false;
        }
    }

    private boolean callerIsAnAdministrator() {
        SecurityContentUser userDetails = userDetailsSource.getFromApplicationContext();
        if (userDetails != null) {
            return roleService.hasAdministratorRole(userDetails.getUserId());
        } else {
            return false;
        }
    }
}
