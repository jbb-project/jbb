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

import org.jbb.lib.core.vo.Username;
import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.Member;
import org.jbb.members.api.exception.DisplayedNameException;
import org.jbb.members.api.service.MemberService;
import org.jbb.members.web.base.form.EditProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(value = "/ucp/profile/edit")
public class UcpEditController {
    private static final String EDIT_PROFILE_FORM = "editProfileForm";
    private static final String FORM_SAVED_FLAG = "editProfileFormSaved";

    @Autowired
    private MemberService memberService;

    @RequestMapping(method = RequestMethod.GET)
    public String edit(Model model, Authentication authentication) {
        if (!model.containsAttribute(FORM_SAVED_FLAG)) {
            EditProfileForm editProfileForm = new EditProfileForm();
            User currentUser = (User) authentication.getPrincipal();
            Optional<Member> member = memberService.getMemberWithUsername(Username.builder().value(currentUser.getUsername()).build());
            editProfileForm.setDisplayedName(member.get().getDisplayedName().toString());
            model.addAttribute(EDIT_PROFILE_FORM, editProfileForm);
        }
        return "ucp/profile/edit";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String editPost(Model model, Authentication authentication,
                           @ModelAttribute(EDIT_PROFILE_FORM) EditProfileForm editProfileForm,
                           BindingResult bindingResult) {
        User currentUser = (User) authentication.getPrincipal();
        Username username = Username.builder().value(currentUser.getUsername()).build();
        DisplayedName displayedName = DisplayedName.builder().value(editProfileForm.getDisplayedName()).build();

        try {
            memberService.updateDisplayedName(username, displayedName);
        } catch (DisplayedNameException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            log.debug("Validation error of user input data during registration: {}", violations, e);
            bindingResult.rejectValue("displayedName", "DN", violations.iterator().next().getMessage());
            model.addAttribute(FORM_SAVED_FLAG, false);
            return "ucp/profile/edit";
        }

        model.addAttribute(FORM_SAVED_FLAG, true);
        return "ucp/profile/edit";
    }
}
