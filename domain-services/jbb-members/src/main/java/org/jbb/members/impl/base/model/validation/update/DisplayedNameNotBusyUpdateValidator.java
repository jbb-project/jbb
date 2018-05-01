/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.model.validation.update;

import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.security.api.role.RoleService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DisplayedNameNotBusyUpdateValidator implements
        ConstraintValidator<DisplayedNameNotBusyUpdate, MemberEntity> {

    private final MemberRepository memberRepository;
    private final UserDetailsSource userDetailsSource;
    private final RoleService roleService;

    private String message;

    @Override
    public void initialize(DisplayedNameNotBusyUpdate displayedNameNotBusy) {
        message = displayedNameNotBusy.message();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(MemberEntity memberEntity, ConstraintValidatorContext context) {
        Long memberId = memberEntity.getId();
        DisplayedName displayedName = memberEntity.getDisplayedName();
        Optional<MemberEntity> memberWithDisplayedName = memberRepository
                .findByDisplayedName(displayedName);

        boolean result = !memberWithDisplayedName.isPresent()
                || (editsProperMember(memberWithDisplayedName.get(), memberId)
                && (currentUserIsUsing(displayedName) || callerIsAnAdministrator()));

        if (!result) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("displayedName").addConstraintViolation();
        }
        return result;
    }

    private boolean editsProperMember(MemberEntity memberEntity, Long memberId) {
        return memberEntity.getId().equals(memberId);
    }

    private boolean currentUserIsUsing(DisplayedName displayedName) {
        SecurityContentUser currentUser = userDetailsSource.getFromApplicationContext();

        if (currentUser != null) {
            Username currentUsername = Username.of(currentUser.getUsername());
            Optional<MemberEntity> currentMember = memberRepository.findByUsername(currentUsername);
            return currentMember.isPresent()
                    && currentMember.get().getDisplayedName().equals(displayedName);
        }
        return false;
    }

    private boolean callerIsAnAdministrator() {
        SecurityContentUser userDetails = userDetailsSource.getFromApplicationContext();
        return userDetails != null && roleService.hasAdministratorRole(userDetails.getUserId());
    }
}
