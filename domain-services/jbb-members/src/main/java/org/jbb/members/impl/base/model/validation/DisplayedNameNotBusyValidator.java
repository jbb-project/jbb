/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.model.validation;

import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.Member;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.security.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class DisplayedNameNotBusyValidator implements ConstraintValidator<DisplayedNameNotBusy, MemberEntity> {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserDetailsSource userDetailsSource;

    @Autowired
    private RoleService roleService;

    private String messageTemplate;

    @Override
    public void initialize(DisplayedNameNotBusy displayedNameNotBusy) {
        messageTemplate = displayedNameNotBusy.message();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(MemberEntity memberEntity, ConstraintValidatorContext constraintValidatorContext) {
        DisplayedName displayedName = memberEntity.getDisplayedName();
        Long counter = memberRepository.countByDisplayedName(displayedName);
        boolean result = counter == 0 || (counter == 1 && (currentUserIsUsing(displayedName) || callerIsAnAdministrator()));

        if (!result) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(messageTemplate)
                    .addPropertyNode("displayedName").addConstraintViolation();
        }
        return result;
    }

    private boolean currentUserIsUsing(DisplayedName displayedName) {
        UserDetails userDetails = userDetailsSource.getFromApplicationContext();
        if (userDetails != null) {
            Username currentUsername = Username.builder().value(userDetails.getUsername()).build();
            Member memberWithDisplayedName = memberRepository.findByDisplayedName(displayedName);
            return memberWithDisplayedName.getUsername().equals(currentUsername);
        }
        return false;
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
