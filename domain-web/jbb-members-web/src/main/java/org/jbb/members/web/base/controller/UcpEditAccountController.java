/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.controller;

import static org.jbb.permissions.api.permission.domain.MemberPermissions.CAN_CHANGE_EMAIL;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.AccountException;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.web.base.data.AccountDataToChangeImpl;
import org.jbb.members.web.base.form.EditAccountForm;
import org.jbb.members.web.base.logic.EditAccountErrorsBindingMapper;
import org.jbb.permissions.api.PermissionService;
import org.jbb.security.api.password.PasswordService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/ucp/profile/editAccount")
public class UcpEditAccountController {
    private static final String VIEW_NAME = "ucp/profile/editAccount";
    private static final String EDIT_ACCOUNT_FORM = "editAccountForm";
    private static final String FORM_SAVED_FLAG = "editAccountFormSaved";
    private static final String EMAIL_FIELD_ENABLED = "hasChangeEmailPermission";

    private final MemberService memberService;
    private final PasswordService passwordService;
    private final PermissionService permissionService;
    private final EditAccountErrorsBindingMapper errorsBindingMapper;

    private static void fillFieldWithUsername(EditAccountForm editAccountForm, Member member) {
        editAccountForm.setUsername(member.getUsername().toString());
    }

    private static boolean passwordChanged(EditAccountForm editAccountForm) {
        return StringUtils.isNotEmpty(editAccountForm.getNewPassword())
                || StringUtils.isNotEmpty(editAccountForm.getNewPasswordAgain());
    }

    private String formViewWithError(Model model) {
        model.addAttribute(FORM_SAVED_FLAG, false);
        model.addAttribute(EMAIL_FIELD_ENABLED,
            permissionService.checkPermission(CAN_CHANGE_EMAIL));
        return VIEW_NAME;
    }

    private boolean emailChanged(Member member, Email newEmail) {
        boolean changed = !member.getEmail().equals(newEmail);
        if (changed) {
            permissionService.assertPermission(CAN_CHANGE_EMAIL);
        }
        return changed;
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

        model.addAttribute(EMAIL_FIELD_ENABLED,
            permissionService.checkPermission(CAN_CHANGE_EMAIL));

        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String editAccountPost(Model model, Authentication authentication,
                                  @ModelAttribute(EDIT_ACCOUNT_FORM) EditAccountForm form,
                                  BindingResult bindingResult) {
        Member member = getCurrentMember(authentication);
        fillFieldWithUsername(form, member);

        // check correction of current password - it is necessary to proceed
        if (currentPasswordIsIncorrect(member.getId(), form.getCurrentPassword())) {
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
            memberService.updateAccount(member.getId(), accountData);
        } catch (AccountException e) {
            log.debug("Problem with updating account for username {} with data to change: {}",
                    member.getUsername(), accountData, e);
            errorsBindingMapper.map(e.getConstraintViolations(), bindingResult);
            return formViewWithError(model);
        }

        model.addAttribute(FORM_SAVED_FLAG, true);
        model.addAttribute(EMAIL_FIELD_ENABLED,
            permissionService.checkPermission(CAN_CHANGE_EMAIL));
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

    private boolean currentPasswordIsIncorrect(Long memberId, String currentPassword) {
        return !passwordService.verifyFor(memberId, Password.builder().value(currentPassword.toCharArray()).build());
    }
}
