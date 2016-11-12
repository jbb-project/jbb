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
import org.jbb.members.api.data.MemberSearchCriteria;
import org.jbb.members.api.exception.MemberSearchJoinDateFormatException;
import org.jbb.members.api.service.MemberService;
import org.jbb.members.web.base.data.AccountDataToChangeImpl;
import org.jbb.members.web.base.data.MemberSearchRow;
import org.jbb.members.web.base.data.ProfileDataToChangeImpl;
import org.jbb.members.web.base.form.EditMemberForm;
import org.jbb.members.web.base.form.SearchMemberForm;
import org.jbb.members.web.base.logic.MemberSearchCriteriaFactory;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class AcpManageMemberController {
    private static final String VIEW_NAME = "acp/members/manage";
    private static final String MEMBERS_SEARCH_FORM = "membersSearchForm";
    private static final String SEARCH_FORM_SENT_FLAG = "memberSearchFormSent";

    private static final String EDIT_VIEW_NAME = "acp/members/edit";
    private static final String EDIT_MEMBER_FORM = "editMemberForm";
    private static final String EDIT_MEMBER_FORM_SENT_FLAG = "editMemberFormSent";


    private final MemberService memberService;
    private final RoleService roleService;
    private final MemberSearchCriteriaFactory criteriaFactory;

    @Autowired
    public AcpManageMemberController(MemberService memberService,
                                     RoleService roleService,
                                     MemberSearchCriteriaFactory criteriaFactory) {
        this.memberService = memberService;
        this.roleService = roleService;
        this.criteriaFactory = criteriaFactory;
    }

    @RequestMapping(value = "/acp/members/manage", method = RequestMethod.GET)
    public String membersSearchGet(Model model,
                                   @ModelAttribute(MEMBERS_SEARCH_FORM) SearchMemberForm form) {
        return VIEW_NAME;
    }

    @RequestMapping(value = "/acp/members/manage", method = RequestMethod.POST)
    public String membersSearchPost(Model model,
                                    @ModelAttribute(MEMBERS_SEARCH_FORM) SearchMemberForm form,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(SEARCH_FORM_SENT_FLAG, false);
            return VIEW_NAME;
        }
        MemberSearchCriteria criteria = criteriaFactory.build(form);
        List<MemberSearchRow> result;
        try {
            result = memberService.getAllMembersWithCriteria(criteria).stream()
                    .map(member ->
                            new MemberSearchRow(member.getId(), member.getUsername(), member.getDisplayedName(),
                                    member.getEmail(), member.getRegistrationMetaData().getJoinDateTime()))
                    .collect(Collectors.toList());
        } catch (MemberSearchJoinDateFormatException e) {
            bindingResult.rejectValue("joinedDate", "acpManageMember", "Specify date in YYYY-MM-DD format");
            model.addAttribute(SEARCH_FORM_SENT_FLAG, false);
            return VIEW_NAME;
        }
        model.addAttribute(SEARCH_FORM_SENT_FLAG, true);
        model.addAttribute("memberRows", result);
        return VIEW_NAME;
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
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return EDIT_VIEW_NAME;
        }
        Optional<Member> memberOptional = memberService.getMemberWithId(form.getId());
        if (!memberOptional.isPresent()) {
            throw new IllegalArgumentException(String.format("User with id %s not found", form.getId()));
        }
        Member member = memberOptional.get();
        AccountDataToChangeImpl accountDataToChange = new AccountDataToChangeImpl();
        accountDataToChange.setEmail(Email.builder().value(form.getEmail()).build());
        if (StringUtils.isNoneBlank(form.getNewPassword())) {
            accountDataToChange.setNewPassword(Password.builder()
                    .value(form.getNewPassword().toCharArray()).build());
        }
        memberService.updateAccount(member.getUsername(), accountDataToChange);

        ProfileDataToChangeImpl profileDataToChange = new ProfileDataToChangeImpl();
        profileDataToChange.setDisplayedName(DisplayedName.builder().value(form.getDisplayedName()).build());
        memberService.updateProfile(member.getUsername(), profileDataToChange);

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
