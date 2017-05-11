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
import org.jbb.board.api.service.BoardService;
import org.jbb.board.web.forum.form.ForumForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/acp/general/forums/forum")
public class AcpForumController {
    private static final String VIEW_NAME = "acp/general/forum";
    private static final String REDIRECT_TO_FORUM_MANAGEMENT = "redirect:/acp/general/forums";

    private static final String FORUM_FORM = "forumForm";

    private final BoardService boardService;

    @Autowired
    public AcpForumController(BoardService boardService) {
        this.boardService = boardService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String generalBoardGet(@RequestParam(value = "id", required = false) Long forumId, Model model) {
        ForumForm form = new ForumForm();
        if (forumId != null) {
            Forum forum = boardService.getForum(forumId);
            if (forum != null) {
                form.setId(forum.getId());
                form.setName(forum.getName());
                form.setDescription(forum.getDescription());
                form.setLocked(forum.isLocked());
            }
        }
        model.addAttribute(FORUM_FORM, form);
        return VIEW_NAME;
    }
}
