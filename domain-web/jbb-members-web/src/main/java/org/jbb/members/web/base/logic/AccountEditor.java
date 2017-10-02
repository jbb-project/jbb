/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.logic;

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Password;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.AccountException;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.web.base.data.AccountDataToChangeImpl;
import org.jbb.members.web.base.form.EditMemberForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AccountEditor {
    private final MemberService memberService;
    private final EditAccountErrorsBindingMapper errorsBindingMapper;

    @Autowired
    public AccountEditor(MemberService memberService,
                         EditAccountErrorsBindingMapper errorsBindingMapper) {
        this.memberService = memberService;
        this.errorsBindingMapper = errorsBindingMapper;
    }

    public boolean editAccountWithSuccess(EditMemberForm form,
                                          BindingResult bindingResult,
                                          Member member) {
        AccountDataToChangeImpl accountDataToChange = new AccountDataToChangeImpl();
        if (!member.getEmail().getValue().equals(form.getEmail())) {
            accountDataToChange.setEmail(Email.builder().value(form.getEmail()).build());
        }
        if (StringUtils.isNoneBlank(form.getNewPassword())) {
            if (!form.getNewPassword().equals(form.getNewPasswordAgain())) {
                bindingResult.rejectValue("newPassword", "NP", "Passwords don't match");
                return false;
            }
            accountDataToChange.setNewPassword(Password.builder()
                    .value(form.getNewPassword().toCharArray()).build());
        }

        try {
            memberService.updateAccount(member.getId(), accountDataToChange);
        } catch (AccountException e) {
            log.debug("Problem with updating account for username {} with data to change: {}",
                    member.getUsername(), accountDataToChange, e);
            errorsBindingMapper.map(e.getConstraintViolations(), bindingResult);
            return false;
        }
        return true;
    }
}
