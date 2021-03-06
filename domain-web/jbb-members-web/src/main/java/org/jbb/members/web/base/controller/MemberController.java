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


import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.registration.MemberRegistrationAware;
import org.jbb.members.web.base.data.MemberBrowserRow;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @RequestMapping(value = "/members")
    public String getMemberBrowser(Model model) {

        List<MemberRegistrationAware> members = memberService.getAllMembersSortedByRegistrationDate();
        List<MemberBrowserRow> memberRows = members.stream()
                .map(member ->
                        new MemberBrowserRow(member.getEmail(), member.getDisplayedName(),
                                member.getRegistrationMetaData().getJoinDateTime()))
                .collect(Collectors.toList());

        model.addAttribute("memberRows", memberRows);
        return "member_browser";
    }

}
