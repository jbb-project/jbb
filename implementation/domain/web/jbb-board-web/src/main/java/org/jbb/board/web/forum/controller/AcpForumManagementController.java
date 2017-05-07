/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.web.forum.controller;

import org.jbb.board.api.model.Forum;
import org.jbb.board.api.model.ForumCategory;
import org.jbb.board.api.service.BoardService;
import org.jbb.board.web.forum.data.ForumCategoryRow;
import org.jbb.board.web.forum.data.ForumRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/acp/general/forums")
public class AcpForumManagementController {
    private static final String VIEW_NAME = "acp/general/forums";

    private final BoardService boardService;

    @Autowired
    public AcpForumManagementController(BoardService boardService) {
        this.boardService = boardService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String generalBoardGet(Model model) {
        List<ForumCategory> forumCategories = boardService.getForumCategories();

        List<ForumCategoryRow> forumStructureRows = forumCategories.stream()
                .map(forumCategory -> createForumDto(forumCategory))
                .collect(Collectors.toList());

        for (int i = 1; i <= forumStructureRows.size(); i++) {
            forumStructureRows.get(i - 1).setPosition(i);
        }

        model.addAttribute("forumStructure", forumStructureRows);

        return VIEW_NAME;
    }

    private ForumCategoryRow createForumDto(ForumCategory forumCategory) {
        ForumCategoryRow categoryRow = new ForumCategoryRow();
        categoryRow.setId(forumCategory.getId());
        categoryRow.setName(forumCategory.getName());

        List<ForumRow> forumRows = forumCategory.getForums().stream()
                .map(forum -> createForumDto(forum))
                .collect(Collectors.toList());

        for (int i = 1; i <= forumRows.size(); i++) {
            forumRows.get(i - 1).setPosition(i);
        }

        categoryRow.setForumRows(forumRows);
        return categoryRow;
    }

    private ForumRow createForumDto(Forum forum) {
        return ForumRow.builder()
                .id(forum.getId())
                .name(forum.getName())
                .description(forum.getDescription())
                .locked(forum.isLocked())
                .build();
    }
}
