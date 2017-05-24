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

import org.jbb.board.api.model.Forum;
import org.jbb.board.api.model.ForumCategory;
import org.jbb.board.api.service.BoardService;
import org.jbb.frontend.web.base.data.ForumCategoryRow;
import org.jbb.frontend.web.base.data.ForumRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomePageController {
    private static final String HOME_VIEW_NAME = "home";

    private final BoardService boardService;

    @Autowired
    public HomePageController(BoardService boardService) {
        this.boardService = boardService;
    }

    @RequestMapping("/")
    public String main(Model model) {
        List<ForumCategory> forumCategories = boardService.getForumCategories();

        List<ForumCategoryRow> forumStructureRows = forumCategories.stream()
                .map(forumCategory -> createDto(forumCategory))
                .collect(Collectors.toList());

        model.addAttribute("forumStructure", forumStructureRows);

        return HOME_VIEW_NAME;
    }

    private ForumCategoryRow createDto(ForumCategory forumCategory) {
        ForumCategoryRow categoryRow = new ForumCategoryRow();
        categoryRow.setName(forumCategory.getName());

        List<ForumRow> forumRows = forumCategory.getForums().stream()
                .map(forum -> createDto(forum))
                .collect(Collectors.toList());

        categoryRow.setForumRows(forumRows);
        return categoryRow;
    }

    private ForumRow createDto(Forum forum) {
        return ForumRow.builder()
                .id(forum.getId())
                .name(forum.getName())
                .description(forum.getDescription())
                .closed(forum.isClosed())
                .build();
    }

    @RequestMapping("/faq")
    public String faq() {
        return "faq"; //NOSONAR
    }
}
