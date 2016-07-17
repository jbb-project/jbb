/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.controllers;

import org.jbb.frontend.api.services.BoardNameService;
import org.jbb.frontend.events.SwitchPageEvent;
import org.jbb.lib.eventbus.JbbEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomePageController {
    @Autowired
    private BoardNameService boardNameService;

    @Autowired
    private JbbEventBus eventBus;

    @RequestMapping("/")
    public String main() {
        publishEvent("home");
        return "home";
    }

    private void publishEvent(String viewName) {
        eventBus.post(new SwitchPageEvent(viewName));
    }

    @RequestMapping("/faq")
    public String faq() {
        publishEvent("faq");
        return "faq";
    }

    @RequestMapping("/signin")
    public String signIn() {
        publishEvent("signin");
        return "signin";
    }

    @RequestMapping("/set")
    public String setBoardName(@RequestParam("newBoardName") String newBoardName) {
        boardNameService.setBoardName(newBoardName);
        return "redirect:/";
    }
}
