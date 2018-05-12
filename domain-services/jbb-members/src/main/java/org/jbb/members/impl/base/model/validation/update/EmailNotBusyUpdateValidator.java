/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.model.validation.update;

import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.impl.base.MembersProperties;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.security.api.privilege.PrivilegeService;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class EmailNotBusyUpdateValidator implements
        ConstraintValidator<EmailNotBusyUpdate, MemberEntity> {

    private final MemberRepository memberRepository;
    private final UserDetailsSource userDetailsSource;
    private final MembersProperties properties;
    private final PrivilegeService privilegeService;

    private String message;

    @Override
    public void initialize(EmailNotBusyUpdate emailNotBusy) {
        message = emailNotBusy.message();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(MemberEntity memberEntity, ConstraintValidatorContext context) {
        Email email = memberEntity.getEmail();
        List<MemberEntity> membersWithEmail = memberRepository.findByEmail(email);

        boolean result = properties.allowEmailDuplication()
                || membersWithEmail.isEmpty()
                || checkRightsToUse(memberEntity, membersWithEmail);

        if (!result) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("email").addConstraintViolation();
        }
        return result;
    }

    private boolean checkRightsToUse(MemberEntity memberEntity,
                                     List<MemberEntity> membersWithEmail) {
        return editsProperMember(membersWithEmail, memberEntity.getId()) && (
                currentUserIsUsing(memberEntity.getEmail()) || callerIsAnAdministrator()
        );
    }

    private boolean editsProperMember(List<MemberEntity> memberEntities, Long memberId) {
        return memberEntities.stream()
                .anyMatch(entity -> entity.getId().equals(memberId));
    }

    private boolean currentUserIsUsing(Email email) {
        SecurityContentUser currentUser = userDetailsSource.getFromApplicationContext();

        if (currentUser != null) {
            Username currentUsername = Username.of(currentUser.getUsername());
            Optional<MemberEntity> currentMember = memberRepository.findByUsername(currentUsername);
            return currentMember.isPresent() && currentMember.get().getEmail().equals(email);
        }
        return false;
    }

    private boolean callerIsAnAdministrator() {
        SecurityContentUser userDetails = userDetailsSource.getFromApplicationContext();
        return userDetails != null && privilegeService
            .hasAdministratorPrivilege(userDetails.getUserId());
    }
}
