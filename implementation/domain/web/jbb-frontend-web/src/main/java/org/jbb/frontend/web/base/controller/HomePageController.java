/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.controller;

import org.jbb.board.api.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePageController {
    private final BoardService boardService;

    @Autowired
    public HomePageController(BoardService boardService) {
        this.boardService = boardService;
    }

    @RequestMapping("/")
    public String main() {
        return "home"; //NOSONAR
    }

    @RequestMapping("/faq")
    public String faq() {
        return "faq"; //NOSONAR
    }
}
