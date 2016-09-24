/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.controller;

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.Password;
import org.jbb.lib.core.vo.Username;
import org.jbb.members.api.data.Member;
import org.jbb.members.api.exception.AccountException;
import org.jbb.members.api.service.MemberService;
import org.jbb.members.web.base.data.AccountDataToChangeImpl;
import org.jbb.members.web.base.form.EditAccountForm;
import org.jbb.members.web.base.logic.EditAccountErrorsBindingMapper;
import org.jbb.security.api.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(value = "/ucp/profile/editAccount")
public class UcpEditAccountController {
    private static final String VIEW_NAME = "ucp/profile/editAccount";
    private static final String EDIT_ACCOUNT_FORM = "editAccountForm";
    private static final String FORM_SAVED_FLAG = "editAccountFormSaved";

    private final MemberService memberService;
    private final PasswordService passwordService;
    private final EditAccountErrorsBindingMapper errorsBindingMapper;

    @Autowired
    public UcpEditAccountController(MemberService memberService, PasswordService passwordService,
                                    EditAccountErrorsBindingMapper errorsBindingMapper) {
        this.memberService = memberService;
        this.passwordService = passwordService;
        this.errorsBindingMapper = errorsBindingMapper;
    }

    private static void fillFieldWithUsername(EditAccountForm editAccountForm, Member member) {
        editAccountForm.setUsername(member.getUsername().toString());
    }

    private static String formViewWithError(Model model) {
        model.addAttribute(FORM_SAVED_FLAG, false);
        return VIEW_NAME;
    }

    private static boolean passwordChanged(EditAccountForm editAccountForm) {
        return StringUtils.isNotEmpty(editAccountForm.getNewPassword())
                || StringUtils.isNotEmpty(editAccountForm.getNewPasswordAgain());
    }

    private static boolean emailChanged(Member member, Email newEmail) {
        return !member.getEmail().equals(newEmail);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String editAccount(Model model, Authentication authentication,
                              @ModelAttribute(EDIT_ACCOUNT_FORM) EditAccountForm form) {
        Member member = getCurrentMember(authentication);
        fillFieldWithUsername(form, member);

        if (!model.containsAttribute(FORM_SAVED_FLAG)) {
            form.setEmail(member.getEmail().toString());
            model.addAttribute(EDIT_ACCOUNT_FORM, form);
        }

        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String editAccountPost(Model model, Authentication authentication,
                                  @ModelAttribute(EDIT_ACCOUNT_FORM) EditAccountForm form,
                                  BindingResult bindingResult) {
        Member member = getCurrentMember(authentication);
        fillFieldWithUsername(form, member);

        // check correction of current password - it is necessary to proceed
        if (currentPasswordIsIncorrect(member.getUsername(), form.getCurrentPassword())) {
            bindingResult.rejectValue("currentPassword", "CP", "Given password is not match to current password");
            return formViewWithError(model);
        }

        // detect which data user want to update
        AccountDataToChangeImpl accountData = new AccountDataToChangeImpl();
        Email newEmail = Email.builder().value(form.getEmail()).build();
        if (emailChanged(member, newEmail)) {
            accountData.setEmail(newEmail);
        }

        if (passwordChanged(form)) {
            Password newPassword = Password.builder().value(form.getNewPassword().toCharArray()).build();
            Password newPasswordAgain = Password.builder().value(form.getNewPasswordAgain().toCharArray()).build();
            if (!newPassword.equals(newPasswordAgain)) {
                bindingResult.rejectValue("newPassword", "NP", "Passwords don't match");
                return formViewWithError(model);
            } else {
                accountData.setNewPassword(newPassword);
            }
        }

        // invoke service for updating data
        try {
            memberService.updateAccount(member.getUsername(), accountData);
        } catch (AccountException e) {
            log.debug("Problem with updating account for username {} with data to change: {}",
                    member.getUsername(), accountData, e);
            errorsBindingMapper.map(e.getConstraintViolations(), bindingResult);
            return formViewWithError(model);
        }

        model.addAttribute(FORM_SAVED_FLAG, true);
        return VIEW_NAME;
    }

    private Member getCurrentMember(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Optional<Member> member = memberService.getMemberWithUsername(Username.builder().value(currentUser.getUsername()).build());
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new UsernameNotFoundException(String.format("User with username '%s' not found", currentUser.getUsername()));
        }
    }

    private boolean currentPasswordIsIncorrect(Username username, String currentPassword) {
        return !passwordService.verifyFor(username, Password.builder().value(currentPassword.toCharArray()).build());
    }
}
