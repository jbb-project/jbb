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

import static org.jbb.permissions.api.permission.domain.MemberPermissions.CAN_CHANGE_DISPLAYED_NAME;

import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.mvc.security.SecurityContextHelper;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.base.ProfileException;
import org.jbb.members.web.base.data.ProfileDataToChangeImpl;
import org.jbb.members.web.base.form.EditProfileForm;
import org.jbb.permissions.api.PermissionService;
import org.jbb.permissions.api.exceptions.PermissionRequiredException;
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
@RequestMapping(value = "/ucp/profile/edit")
public class UcpEditController {
    private static final String VIEW_NAME = "ucp/profile/edit";
    private static final String EDIT_PROFILE_FORM = "editProfileForm";
    private static final String FORM_SAVED_FLAG = "editProfileFormSaved";
    private static final String DISPLAYED_NAME_FIELD_ENABLED = "hasDisplayedNameChangePermission";

    private final MemberService memberService;
    private final PermissionService permissionService;
    private final SecurityContextHelper securityContextHelper;

    @RequestMapping(method = RequestMethod.GET)
    public String edit(Model model, Authentication authentication) {
        if (!model.containsAttribute(FORM_SAVED_FLAG)) {
            EditProfileForm editProfileForm = new EditProfileForm();
            User currentUser = (User) authentication.getPrincipal();
            Optional<Member> member = memberService.getMemberWithUsername(Username.builder().value(currentUser.getUsername()).build());

            if (!member.isPresent()) {
                throw new UsernameNotFoundException(String.format("User with username '%s' not found", currentUser.getUsername()));
            }

            editProfileForm.setDisplayedName(member.get().getDisplayedName().toString());
            model.addAttribute(EDIT_PROFILE_FORM, editProfileForm);
        }
        model.addAttribute(DISPLAYED_NAME_FIELD_ENABLED, permissionService.checkPermission(
            CAN_CHANGE_DISPLAYED_NAME));
        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String editPost(Model model, Authentication authentication,
                           @ModelAttribute(EDIT_PROFILE_FORM) EditProfileForm editProfileForm,
                           BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        SecurityContentUser currentUser = (SecurityContentUser) authentication.getPrincipal();
        DisplayedName displayedName = DisplayedName.builder().value(editProfileForm.getDisplayedName()).build();

        ProfileDataToChangeImpl profileDataToChange = new ProfileDataToChangeImpl();
        profileDataToChange.setDisplayedName(displayedName);

        if (profileDataToChange.getDisplayedName().isPresent() &&
            !currentUser.getDisplayedName()
                .equals(profileDataToChange.getDisplayedName().get().getValue()) &&
            !permissionService.checkPermission(CAN_CHANGE_DISPLAYED_NAME)) {
            throw new PermissionRequiredException(CAN_CHANGE_DISPLAYED_NAME);
        }

        try {
            memberService.updateProfile(currentUser.getUserId(), profileDataToChange);
        } catch (ProfileException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            log.debug("Validation error of user input data during registration: {}", violations, e);
            bindingResult.rejectValue("displayedName", "DN", violations.iterator().next().getMessage());
            model.addAttribute(DISPLAYED_NAME_FIELD_ENABLED, permissionService.checkPermission(
                CAN_CHANGE_DISPLAYED_NAME));
            model.addAttribute(FORM_SAVED_FLAG, false);
            return VIEW_NAME;
        }

        model.addAttribute(FORM_SAVED_FLAG, true);
        model.addAttribute(DISPLAYED_NAME_FIELD_ENABLED, permissionService.checkPermission(
            CAN_CHANGE_DISPLAYED_NAME));
        securityContextHelper.refresh(request, response);
        return VIEW_NAME;
    }
}
