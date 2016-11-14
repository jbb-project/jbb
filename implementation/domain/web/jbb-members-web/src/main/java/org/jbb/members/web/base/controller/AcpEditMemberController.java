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
import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.Member;
import org.jbb.members.api.exception.AccountException;
import org.jbb.members.api.exception.ProfileException;
import org.jbb.members.api.service.MemberService;
import org.jbb.members.web.base.data.AccountDataToChangeImpl;
import org.jbb.members.web.base.data.ProfileDataToChangeImpl;
import org.jbb.members.web.base.form.EditMemberForm;
import org.jbb.members.web.base.logic.EditAccountErrorsBindingMapper;
import org.jbb.security.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AcpEditMemberController {
    private static final String EDIT_VIEW_NAME = "acp/members/edit";
    private static final String EDIT_MEMBER_FORM = "editMemberForm";
    private static final String EDIT_MEMBER_FORM_SENT_FLAG = "editMemberFormSent";

    private final MemberService memberService;
    private final RoleService roleService;
    private final EditAccountErrorsBindingMapper errorsBindingMapper;

    @Autowired
    public AcpEditMemberController(MemberService memberService,
                                   RoleService roleService,
                                   EditAccountErrorsBindingMapper errorsBindingMapper) {
        this.memberService = memberService;
        this.roleService = roleService;
        this.errorsBindingMapper = errorsBindingMapper;
    }

    @RequestMapping(value = "/acp/members/edit", method = RequestMethod.GET)
    public String editMemberGet(@RequestParam(value = "id") Long memberId, Model model) {
        Optional<Member> memberOptional = memberService.getMemberWithId(memberId);

        if (!memberOptional.isPresent()) {
            throw new IllegalArgumentException(String.format("User with id %s not found", memberId));
        }

        if (!model.containsAttribute(EDIT_MEMBER_FORM)) {
            EditMemberForm form = new EditMemberForm();

            Member member = memberOptional.get();
            form.setId(member.getId());
            form.setUsername(member.getUsername().getValue());
            form.setDisplayedName(member.getDisplayedName().getValue());
            form.setEmail(member.getEmail().getValue());
            form.setHasAdminRole(roleService.hasAdministratorRole(member.getUsername()));

            model.addAttribute(EDIT_MEMBER_FORM, form);
        }

        return EDIT_VIEW_NAME;
    }

    @RequestMapping(value = "/acp/members/edit", method = RequestMethod.POST)
    public String editMemberPost(@ModelAttribute(EDIT_MEMBER_FORM) EditMemberForm form,
                                 RedirectAttributes redirectAttributes,
                                 BindingResult bindingResult) {
        Optional<Member> memberOptional = memberService.getMemberWithId(form.getId());
        if (!memberOptional.isPresent()) {
            throw new IllegalArgumentException(String.format("User with id %s not found", form.getId()));
        }
        Member member = memberOptional.get();
        form.setUsername(member.getUsername().getValue());

        if (bindingResult.hasErrors()) {
            return EDIT_VIEW_NAME;
        }

        // edit account
        AccountDataToChangeImpl accountDataToChange = new AccountDataToChangeImpl();
        if (!member.getEmail().getValue().equals(form.getEmail())) {
            accountDataToChange.setEmail(Email.builder().value(form.getEmail()).build());
        }
        if (StringUtils.isNoneBlank(form.getNewPassword())) {
            if (!form.getNewPassword().equals(form.getNewPasswordAgain())) {
                bindingResult.rejectValue("newPassword", "NP", "Passwords don't match");
                redirectAttributes.addFlashAttribute(EDIT_MEMBER_FORM_SENT_FLAG, false);
                return EDIT_VIEW_NAME;
            }
            accountDataToChange.setNewPassword(Password.builder()
                    .value(form.getNewPassword().toCharArray()).build());
        }

        try {
            memberService.updateAccount(member.getUsername(), accountDataToChange);
        } catch (AccountException e) {
            log.debug("Problem with updating account for username {} with data to change: {}",
                    member.getUsername(), accountDataToChange, e);
            errorsBindingMapper.map(e.getConstraintViolations(), bindingResult);
            redirectAttributes.addFlashAttribute(EDIT_MEMBER_FORM_SENT_FLAG, false);
            return EDIT_VIEW_NAME;
        }

        // edit profile
        ProfileDataToChangeImpl profileDataToChange = new ProfileDataToChangeImpl();
        profileDataToChange.setDisplayedName(DisplayedName.builder().value(form.getDisplayedName()).build());
        try {
            if (!form.getDisplayedName().equals(member.getDisplayedName().getValue())) {
                memberService.updateProfile(member.getUsername(), profileDataToChange);
            }
        } catch (ProfileException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            log.debug("Validation error of user input data during registration: {}", violations, e);
            bindingResult.rejectValue("displayedName", "DN", violations.iterator().next().getMessage());
            redirectAttributes.addFlashAttribute(EDIT_MEMBER_FORM_SENT_FLAG, false);
            return EDIT_VIEW_NAME;
        }

        // edit role
        if (form.isHasAdminRole()) {
            roleService.addAdministratorRole(member.getUsername());
        } else {
            roleService.removeAdministratorRole(member.getUsername());
        }

        redirectAttributes.addAttribute("id", form.getId());
        redirectAttributes.addFlashAttribute(EDIT_MEMBER_FORM, form);
        redirectAttributes.addFlashAttribute(EDIT_MEMBER_FORM_SENT_FLAG, true);

        return "redirect:/" + EDIT_VIEW_NAME;
    }
}
