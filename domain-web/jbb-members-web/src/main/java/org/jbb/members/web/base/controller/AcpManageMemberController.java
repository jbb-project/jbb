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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.lib.mvc.PageWrapper;
import org.jbb.members.api.base.MemberSearchCriteria;
import org.jbb.members.api.base.MemberSearchJoinDateFormatException;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.web.base.data.MemberSearchRow;
import org.jbb.members.web.base.form.SearchMemberForm;
import org.jbb.members.web.base.logic.MemberSearchCriteriaFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AcpManageMemberController {
    public static final String VIEW_NAME = "acp/members/manage";
    private static final String MEMBERS_SEARCH_FORM = "membersSearchForm";
    private static final String SEARCH_FORM_SENT_FLAG = "memberSearchFormSent";

    private final MemberService memberService;
    private final MemberSearchCriteriaFactory criteriaFactory;

    @RequestMapping(value = "/acp/members/manage", method = RequestMethod.GET)
    public String membersSearchGet(Model model,
        @ModelAttribute(MEMBERS_SEARCH_FORM) SearchMemberForm form) {
        model.addAttribute(MEMBERS_SEARCH_FORM, form);
        return VIEW_NAME;
    }

    @RequestMapping(value = "/acp/members/manage", method = RequestMethod.POST)
    public String membersSearchPost(Model model,
        @ModelAttribute(MEMBERS_SEARCH_FORM) SearchMemberForm form, Pageable pageable,
                                    BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(SEARCH_FORM_SENT_FLAG, false);
            return VIEW_NAME;
        }
        MemberSearchCriteria criteria = criteriaFactory.buildCriteria(form, pageable);
        Page<MemberSearchRow> memberPage;
        try {
            memberPage = memberService.getAllMembersWithCriteria(criteria)
                .map(member ->
                    new MemberSearchRow(member.getId(), member.getUsername(),
                        member.getDisplayedName(),
                        member.getEmail(), member.getRegistrationMetaData().getJoinDateTime()));
        } catch (MemberSearchJoinDateFormatException e) {
            log.debug("Incorrect date format entered during member search, value: '{}'. Stacktrace for debugging", form.getJoinedDate(), e);
            bindingResult.rejectValue("joinedDate", "acpManageMember", "Specify date in YYYY-MM-DD format");
            model.addAttribute(SEARCH_FORM_SENT_FLAG, false);
            return VIEW_NAME;
        }
        redirectAttributes.addFlashAttribute(SEARCH_FORM_SENT_FLAG, true);
        redirectAttributes
            .addFlashAttribute("memberPage", new PageWrapper<>(memberPage, "/acp/members/manage"));
        redirectAttributes.addFlashAttribute(MEMBERS_SEARCH_FORM, form);
        return "redirect:/" + VIEW_NAME;
    }

}
