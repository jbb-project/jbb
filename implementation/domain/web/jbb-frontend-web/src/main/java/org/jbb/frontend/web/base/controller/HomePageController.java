/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.controller;

import org.jbb.frontend.api.service.BoardNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomePageController {
    private final BoardNameService boardNameService;

    @Autowired
    public HomePageController(BoardNameService boardNameService) {
        this.boardNameService = boardNameService;
    }

    @RequestMapping("/")
    public String main() {
        return "home";
    }

    @RequestMapping("/faq")
    public String faq() {
        return "faq";
    }

    @RequestMapping("/set")
    public String setBoardName(@RequestParam("newBoardName") String newBoardName) {
        boardNameService.setBoardName(newBoardName);
        return "redirect:/";
    }
}
