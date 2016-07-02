/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.core.controllers;

import org.jbb.frontend.core.services.BoardNameService;
import org.jbb.frontend.events.SwitchPageEvent;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.lib.mvc.JbbMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomePageController {
    @Autowired
    private BoardNameService boardNameService;

    @Autowired
    private JbbMetaData jbbMetaData;

    @Autowired
    private JbbEventBus eventBus;

    @RequestMapping("/")
    public String main(Model model) {
        model.addAttribute("boardName", boardNameService.getBoardName());
        publishEvent("home");
        return "home";
    }

    private void publishEvent(String viewName) {
        eventBus.post(new SwitchPageEvent(viewName));
    }

    @RequestMapping("/faq")
    public String faq(Model model) {
        model.addAttribute("boardName", boardNameService.getBoardName());
        publishEvent("faq");
        return "faq";
    }

    @RequestMapping("/members")
    public String members(Model model) {
        model.addAttribute("boardName", boardNameService.getBoardName());
        publishEvent("members");
        return "members";
    }

    @RequestMapping("/signin")
    public String signIn(Model model) {
        model.addAttribute("boardName", boardNameService.getBoardName());
        publishEvent("signin");
        return "signin";
    }

    @RequestMapping("/register")
    public String signUp(Model model) {
        model.addAttribute("boardName", boardNameService.getBoardName());
        publishEvent("register");
        return "register";
    }

    @RequestMapping("/set")
    public String setBoardName(@RequestParam("newBoardName") String newBoardName) {
        boardNameService.setBoardName(newBoardName);
        return "redirect:/";
    }
}
