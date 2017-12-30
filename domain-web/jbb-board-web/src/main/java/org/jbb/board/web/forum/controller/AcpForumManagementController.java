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

import org.jbb.board.api.forum.BoardService;
import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.web.forum.data.ForumCategoryRow;
import org.jbb.board.web.forum.data.ForumRow;
import org.jbb.permissions.api.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_ADD_FORUMS;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_DELETE_FORUMS;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_MODIFY_FORUMS;

@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/general/forums")
public class AcpForumManagementController {
    private static final String VIEW_NAME = "acp/general/forums";
    private static final String ADD_POSSIBLE = "hasPermissionToAdd";
    private static final String EDIT_POSSIBLE = "hasPermissionToEdit";
    private static final String DELETE_POSSIBLE = "hasPermissionToDelete";

    private final BoardService boardService;
    private final PermissionService permissionService;

    @RequestMapping(method = RequestMethod.GET)
    public String generalBoardGet(Model model) {
        List<ForumCategory> forumCategories = boardService.getForumCategories();

        List<ForumCategoryRow> forumStructureRows = forumCategories.stream()
            .map(this::createForumDto)
                .collect(Collectors.toList());

        for (int i = 1; i <= forumStructureRows.size(); i++) {
            forumStructureRows.get(i - 1).setPosition(i);
        }

        model.addAttribute("forumStructure", forumStructureRows);
        model.addAttribute(ADD_POSSIBLE, permissionService.checkPermission(CAN_ADD_FORUMS));
        model.addAttribute(EDIT_POSSIBLE, permissionService.checkPermission(CAN_MODIFY_FORUMS));
        model.addAttribute(DELETE_POSSIBLE, permissionService.checkPermission(CAN_DELETE_FORUMS));

        return VIEW_NAME;
    }

    private ForumCategoryRow createForumDto(ForumCategory forumCategory) {
        ForumCategoryRow categoryRow = new ForumCategoryRow();
        categoryRow.setId(forumCategory.getId());
        categoryRow.setName(forumCategory.getName());

        List<ForumRow> forumRows = forumCategory.getForums().stream()
            .map(this::createForumDto)
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
                .closed(forum.isClosed())
                .build();
    }
}
