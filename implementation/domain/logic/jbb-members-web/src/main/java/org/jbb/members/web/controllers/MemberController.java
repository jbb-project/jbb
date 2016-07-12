/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.controllers;


import org.jbb.members.api.model.Member;
import org.jbb.members.api.services.MemberService;
import org.jbb.members.api.services.RegistrationService;
import org.jbb.members.web.model.MemberBrowserRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;


@Controller
public class MemberController {

    private final MemberService memberService;

    private final RegistrationService registrationService;

    @Autowired
    public MemberController(MemberService memberService, RegistrationService registrationService) {
        this.memberService = memberService;
        this.registrationService = registrationService;
    }


    @RequestMapping(value = "/members")
    public String getMemberBrowser(Model model) {
        List<Member> members = memberService.getAllMembersSortedByRegistrationDate();
        List<MemberBrowserRow> memberRows = members.stream()
                .map(member ->
                        new MemberBrowserRow(member.getEmail(), member.getDisplayedName(),
                                registrationService.getRegistrationInfo(member).get().getRegistrationDate()))
                .collect(Collectors.toList());

        model.addAttribute("members", memberRows);
        return "member_browser";
    }

}
