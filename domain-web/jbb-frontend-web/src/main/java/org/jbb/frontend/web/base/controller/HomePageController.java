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

import org.jbb.board.api.forum.BoardService;
import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.frontend.web.base.data.ForumCategoryRow;
import org.jbb.frontend.web.base.data.ForumRow;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomePageController {
    private static final String HOME_VIEW_NAME = "home";

    private final BoardService boardService;

    @RequestMapping("/")
    public String main(Model model) {
        List<ForumCategory> forumCategories = boardService.getForumCategories();

        List<ForumCategoryRow> forumStructureRows = forumCategories.stream()
                .map(this::createDto)
                .collect(Collectors.toList());

        model.addAttribute("forumStructure", forumStructureRows);

        return HOME_VIEW_NAME;
    }

    private ForumCategoryRow createDto(ForumCategory forumCategory) {
        ForumCategoryRow categoryRow = new ForumCategoryRow();
        categoryRow.setName(forumCategory.getName());

        List<ForumRow> forumRows = forumCategory.getForums().stream()
                .map(this::createDto)
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

}
