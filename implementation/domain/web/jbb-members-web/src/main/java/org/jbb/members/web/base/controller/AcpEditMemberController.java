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

import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.Member;
import org.jbb.members.api.exception.ProfileException;
import org.jbb.members.api.service.MemberService;
import org.jbb.members.web.base.data.ProfileDataToChangeImpl;
import org.jbb.members.web.base.form.EditMemberForm;
import org.jbb.members.web.base.form.RemoveMemberForm;
import org.jbb.members.web.base.form.UserLockDetailsForm;
import org.jbb.members.web.base.logic.AccountEditor;
import org.jbb.security.api.service.RoleService;
import org.jbb.security.api.service.UserLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
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
    private static final String REMOVE_MEMBER_FORM = "removeMemberForm";
    private static final String GET_MEMBER_ACCOUNT_LOCK_FORM = "userLockDetailsForm";
    private static final String REMOVE_LOCK_FORM = "removeLock";

    private final UserLockService userLockService;
    private final MemberService memberService;
    private final RoleService roleService;
    private final AccountEditor accountEditor;

    @Autowired
    public AcpEditMemberController(UserLockService userLockService,
                                   MemberService memberService,
                                   RoleService roleService,
                                   AccountEditor accountEditor) {
        this.userLockService = userLockService;
        this.memberService = memberService;
        this.roleService = roleService;
        this.accountEditor = accountEditor;
    }

    @RequestMapping(value = "/acp/members/getlock", method = RequestMethod.GET)
    public String getUserAccountLock(@RequestParam(value = "id") Long memberId, Model model) {

        Optional<LocalDateTime> userLockExpireTime = userLockService.getUserLockExpireTime(memberId);
        userLockExpireTime.ifPresent(lockExpiredTime -> model.addAttribute("lockExpiredTime", lockExpiredTime));

        return "redirect:/" + EDIT_VIEW_NAME;
    }

    @RequestMapping(value = "/acp/members/edit", method = RequestMethod.GET)
    public String editMemberGet(@RequestParam(value = "id") Long memberId, Model model) {
        Optional<Member> memberOptional = memberService.getMemberWithId(memberId);

        if (!memberOptional.isPresent()) {
            throw new IllegalArgumentException(String.format("User with id %s not found", memberId));
        }

        if (!model.containsAttribute(EDIT_MEMBER_FORM)) {
            addEditForm(memberOptional, model);
            addUserLockDetailsForm(memberOptional, model);
        }

        model.addAttribute(REMOVE_MEMBER_FORM, new RemoveMemberForm(memberId));
        model.addAttribute(REMOVE_MEMBER_FORM, new RemoveMemberForm(memberId));

        return EDIT_VIEW_NAME;
    }

    private void addUserLockDetailsForm(Optional<Member> memberOptional, Model model) {
        UserLockDetailsForm form = new UserLockDetailsForm();

        Optional<LocalDateTime> userLockExpireTime = userLockService.getUserLockExpireTime(memberOptional.get().getId());
        userLockExpireTime.ifPresent(expireTime -> form.setExpireTime(expireTime));

        model.addAttribute(GET_MEMBER_ACCOUNT_LOCK_FORM, form);
    }

    private void addEditForm(Optional<Member> memberOptional, Model model) {
        EditMemberForm form = new EditMemberForm();

        Member member = memberOptional.get();
        form.setId(member.getId());
        form.setUsername(member.getUsername().getValue());
        form.setDisplayedName(member.getDisplayedName().getValue());
        form.setEmail(member.getEmail().getValue());
        form.setHasAdminRole(roleService.hasAdministratorRole(member.getId()));

        model.addAttribute(EDIT_MEMBER_FORM, form);
    }

    @RequestMapping(value = "/acp/members/edit", method = RequestMethod.POST)
    public String editMemberPost(@ModelAttribute(EDIT_MEMBER_FORM) EditMemberForm form,
                                 RedirectAttributes redirectAttributes,
                                 BindingResult bindingResult, Model model) {
        Optional<Member> memberOptional = memberService.getMemberWithId(form.getId());
        if (!memberOptional.isPresent()) {
            throw new IllegalArgumentException(String.format("User with id %s not found", form.getId()));
        }
        Member member = memberOptional.get();
        form.setUsername(member.getUsername().getValue());

        if (bindingResult.hasErrors()) {
            model.addAttribute(REMOVE_MEMBER_FORM, new RemoveMemberForm(form.getId()));
            model.addAttribute(EDIT_MEMBER_FORM_SENT_FLAG, false);
            return EDIT_VIEW_NAME;
        }

        // edit account
        if (!accountEditor.editAccountWithSuccess(form, bindingResult, member)) {
            model.addAttribute(REMOVE_MEMBER_FORM, new RemoveMemberForm(form.getId()));
            model.addAttribute(EDIT_MEMBER_FORM_SENT_FLAG, false);
            return EDIT_VIEW_NAME;
        }

        // edit profile
        if (!editProfileWithSuccess(form, redirectAttributes, bindingResult, member)) {
            model.addAttribute(REMOVE_MEMBER_FORM, new RemoveMemberForm(form.getId()));
            model.addAttribute(EDIT_MEMBER_FORM_SENT_FLAG, false);
            return EDIT_VIEW_NAME;
        }

        // edit role
        editRole(form, member);

        redirectAttributes.addAttribute("id", form.getId());
        redirectAttributes.addFlashAttribute(EDIT_MEMBER_FORM, form);
        redirectAttributes.addFlashAttribute(EDIT_MEMBER_FORM_SENT_FLAG, true);
        redirectAttributes.addFlashAttribute(REMOVE_MEMBER_FORM, new RemoveMemberForm(form.getId()));

        return "redirect:/" + EDIT_VIEW_NAME;
    }

    private void editRole(@ModelAttribute(EDIT_MEMBER_FORM) EditMemberForm form, Member member) {
        if (form.isHasAdminRole()) {
            roleService.addAdministratorRole(member.getId());
        } else {
            roleService.removeAdministratorRole(member.getId());
        }
    }

    private boolean editProfileWithSuccess(EditMemberForm form, RedirectAttributes redirectAttributes,
                                           BindingResult bindingResult, Member member) {
        ProfileDataToChangeImpl profileDataToChange = new ProfileDataToChangeImpl();
        profileDataToChange.setDisplayedName(DisplayedName.builder().value(form.getDisplayedName()).build());
        try {
            if (!form.getDisplayedName().equals(member.getDisplayedName().getValue())) {
                memberService.updateProfile(member.getId(), profileDataToChange);
            }
        } catch (ProfileException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            log.debug("Validation error of user input data during registration: {}", violations, e);
            bindingResult.rejectValue("displayedName", "DN", violations.iterator().next().getMessage());
            redirectAttributes.addFlashAttribute(EDIT_MEMBER_FORM_SENT_FLAG, false);
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/acp/members/remove", method = RequestMethod.POST)
    public String removeMemberPost(@ModelAttribute(REMOVE_MEMBER_FORM) RemoveMemberForm form,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return EDIT_VIEW_NAME;
        }

        memberService.removeMember(form.getId());

        return "redirect:/" + AcpManageMemberController.VIEW_NAME;
    }
}
