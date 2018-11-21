/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.acp.controller;

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.mvc.PageWrapper;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.lockout.LockSearchCriteria;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.web.acp.data.MemberLockRow;
import org.jbb.security.web.acp.form.SearchLockForm;
import org.jbb.security.web.acp.translator.MemberLockRowTranslator;
import org.jbb.security.web.acp.translator.SearchLockCriteriaFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@RequestMapping("/acp/members/locks")
public class AcpMemberLocksController {

    private static final String LOCK_BROWSER_ACP_VIEW = "acp/members/locks";
    private static final String LOCKS_SEARCH_FORM = "lockSearchForm";
    private static final String FORM_SAVED_FLAG = "lockSearchFormSent";

    private final MemberService memberService;
    private final MemberLockoutService memberLockoutService;

    private final SearchLockCriteriaFactory criteriaFactory;
    private final MemberLockRowTranslator memberLockRowTranslator;

    @RequestMapping(method = RequestMethod.GET)
    public String memberLocksGet(Model model,
        @ModelAttribute(LOCKS_SEARCH_FORM) SearchLockForm form) {
        model.addAttribute(LOCKS_SEARCH_FORM, form);
        return LOCK_BROWSER_ACP_VIEW;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String memberLocksPost(@ModelAttribute(LOCKS_SEARCH_FORM) SearchLockForm form,
        Pageable pageable,
        BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        Long memberId = null;
        if (StringUtils.isNotBlank(form.getDisplayedName())) {
            Optional<Member> member = memberService
                .getMemberWithDisplayedName(DisplayedName.of(form.getDisplayedName()));
            if (member.isPresent()) {
                memberId = member.get().getId();
            } else {
                bindingResult.rejectValue("displayedName", "x", "Member not found");
                return LOCK_BROWSER_ACP_VIEW;
            }
        }

        LockSearchCriteria criteria = criteriaFactory
            .buildCriteria(memberId, form.getStatus(), pageable);
        Page<MemberLockRow> resultPage = memberLockoutService.getLocksWithCriteria(criteria)
            .map(memberLockRowTranslator::toRow);

        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        redirectAttributes
            .addFlashAttribute("resultPage", new PageWrapper<>(resultPage));
        redirectAttributes.addFlashAttribute(LOCKS_SEARCH_FORM, form);

        return "redirect:/" + LOCK_BROWSER_ACP_VIEW;
    }

}
