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

import org.jbb.members.api.service.MemberService;
import org.jbb.members.web.base.form.SearchMemberForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/acp/members/manage")
public class AcpManageMemberController {
    private static final String VIEW_NAME = "acp/members/manage";
    private static final String MEMBERS_SEARCH_FORM = "membersSearchForm";
    private static final String SEARCH_FORM_SENT_FLAG = "memberSearchFormSent";

    private final MemberService memberService;

    @Autowired
    public AcpManageMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String membersSearchGet(Model model,
                                   @ModelAttribute(MEMBERS_SEARCH_FORM) SearchMemberForm form) {
        return VIEW_NAME;
    }
}
