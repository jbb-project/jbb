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

import org.jbb.members.api.data.MemberSearchCriteria;
import org.jbb.members.api.service.MemberService;
import org.jbb.members.web.base.data.MemberSearchRow;
import org.jbb.members.web.base.form.SearchMemberForm;
import org.jbb.members.web.base.logic.MemberSearchCriteriaFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/acp/members/manage")
public class AcpManageMemberController {
    private static final String VIEW_NAME = "acp/members/manage";
    private static final String MEMBERS_SEARCH_FORM = "membersSearchForm";
    private static final String SEARCH_FORM_SENT_FLAG = "memberSearchFormSent";

    private final MemberService memberService;
    private final MemberSearchCriteriaFactory criteriaFactory;

    @Autowired
    public AcpManageMemberController(MemberService memberService,
                                     MemberSearchCriteriaFactory criteriaFactory) {
        this.memberService = memberService;
        this.criteriaFactory = criteriaFactory;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String membersSearchGet(Model model,
                                   @ModelAttribute(MEMBERS_SEARCH_FORM) SearchMemberForm form) {
        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String membersSearchPost(Model model,
                                    @ModelAttribute(MEMBERS_SEARCH_FORM) SearchMemberForm form,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(SEARCH_FORM_SENT_FLAG, false);
            return VIEW_NAME;
        }
        MemberSearchCriteria criteria = criteriaFactory.build(form);
        List<MemberSearchRow> result = memberService.getAllMembersWithCriteria(criteria).stream()
                .map(member ->
                        new MemberSearchRow(member.getUsername(), member.getDisplayedName(),
                                member.getEmail(), member.getRegistrationMetaData().getJoinDateTime()))
                .collect(Collectors.toList());
        model.addAttribute(SEARCH_FORM_SENT_FLAG, true);
        model.addAttribute("memberRows", result);
        return VIEW_NAME;
    }
}
