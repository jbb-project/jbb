/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.web.forum.controller;

import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_ADD_FORUMS;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_DELETE_FORUMS;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_MODIFY_FORUMS;

import com.google.common.collect.Iterables;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.board.api.forum.BoardService;
import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.api.forum.ForumCategoryService;
import org.jbb.board.api.forum.ForumException;
import org.jbb.board.api.forum.ForumService;
import org.jbb.board.web.forum.data.ForumCategoryRow;
import org.jbb.board.web.forum.data.ForumRow;
import org.jbb.board.web.forum.form.ForumDeleteForm;
import org.jbb.board.web.forum.form.ForumForm;
import org.jbb.lib.mvc.SimpleErrorsBindingMapper;
import org.jbb.permissions.api.PermissionService;
import org.jbb.permissions.api.annotation.AdministratorPermissionRequired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/general/forums/forum")
public class AcpForumController {
    private static final String VIEW_NAME = "acp/general/forum";
    private static final String DELETE_VIEW_NAME = "acp/general/forum-delete";
    private static final String REDIRECT_TO_FORUM_MANAGEMENT = "redirect:/acp/general/forums";
    private static final String REDIRECT_TO_FORUM_VIEW = "redirect:/acp/general/forums/forum";

    private static final String FORUM_FORM = "forumForm";
    private static final String FORUM_DELETE_FORM = "forumCategoryDeleteForm";
    private static final String FORUM_ROW = "forum";
    private static final String EDIT_POSSIBLE = "hasPermissionToEdit";

    private final BoardService boardService;
    private final ForumService forumService;
    private final ForumCategoryService forumCategoryService;
    private final PermissionService permissionService;
    private final SimpleErrorsBindingMapper errorMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String forumGet(@RequestParam(value = "id", required = false) Long forumId, Model model) {
        ForumForm form = new ForumForm();

        List<ForumCategory> allCategories = boardService.getForumCategories();
        List<ForumCategoryRow> categoryDtos = allCategories.stream()
                .map(this::mapToForumCategoryDto)
                .collect(Collectors.toList());
        model.addAttribute("availableCategories", categoryDtos);

        if (forumId == null) {
            model.addAttribute(EDIT_POSSIBLE, permissionService.checkPermission(CAN_ADD_FORUMS));
            form.setCategoryId(Iterables.get(allCategories, 0).getId());
        } else {
            model.addAttribute(EDIT_POSSIBLE, permissionService.checkPermission(CAN_MODIFY_FORUMS));
            Forum forum = forumService.getForum(forumId);
            form.setId(forum.getId());
            form.setName(forum.getName());
            form.setDescription(forum.getDescription());
            form.setClosed(forum.isClosed());

            ForumCategory category = forumCategoryService.getCategoryWithForum(forum);
            form.setCategoryId(category.getId());
        }

        if (!model.containsAttribute(FORUM_FORM)) {
            model.addAttribute(FORUM_FORM, form);
        }
        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String forumPost(@ModelAttribute(FORUM_FORM) ForumForm form, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        Forum forum = form.buildForum();
        boolean updateMode = form.getId() != null;

        try {
            if (updateMode) {
                permissionService.assertPermission(CAN_MODIFY_FORUMS);
                forumService.editForum(forum);

                Forum forumEntity = forumService.getForum(form.getId());
                ForumCategory currentCategory = forumCategoryService.getCategoryWithForum(forumEntity);
                if (!Objects.equals(form.getCategoryId(), currentCategory.getId())) {
                    forumService.moveForumToAnotherCategory(forum.getId(), form.getCategoryId());
                }
            } else {
                permissionService.assertPermission(CAN_ADD_FORUMS);
                Optional<ForumCategory> category = forumCategoryService.getCategory(form.getCategoryId());
                forumService.addForum(forum, category.orElseThrow(() -> badCategoryId(form.getCategoryId())));
            }
        } catch (ForumException e) {
            log.debug("Error during add/update forum: {}", e);
            redirectAttributes.addAttribute("id", form.getId());
            errorMapper.map(e.getConstraintViolations(), bindingResult);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + FORUM_FORM, bindingResult);
            redirectAttributes.addFlashAttribute(FORUM_FORM, form);
            redirectAttributes.addFlashAttribute(EDIT_POSSIBLE, updateMode ?
                    permissionService.checkPermission(CAN_MODIFY_FORUMS) :
                    permissionService.checkPermission(CAN_ADD_FORUMS));
            return REDIRECT_TO_FORUM_VIEW;
        }

        return REDIRECT_TO_FORUM_MANAGEMENT;
    }

    @AdministratorPermissionRequired(CAN_MODIFY_FORUMS)
    @RequestMapping(path = "/moveup", method = RequestMethod.POST)
    public String forumMoveUpPost(@ModelAttribute(FORUM_ROW) ForumRow forumRow) {
        Forum forumEntity = forumService.getForum(forumRow.getId());
        forumService.moveForumToPosition(forumEntity, forumRow.getPosition() - 1);
        return REDIRECT_TO_FORUM_MANAGEMENT;
    }

    @AdministratorPermissionRequired(CAN_MODIFY_FORUMS)
    @RequestMapping(path = "/movedown", method = RequestMethod.POST)
    public String forumMoveDownPost(@ModelAttribute(FORUM_ROW) ForumRow forumRow) {
        Forum forumEntity = forumService.getForum(forumRow.getId());
        forumService.moveForumToPosition(forumEntity, forumRow.getPosition() + 1);
        return REDIRECT_TO_FORUM_MANAGEMENT;
    }

    @AdministratorPermissionRequired(CAN_DELETE_FORUMS)
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String forumDelete(Model model, @ModelAttribute(FORUM_ROW) ForumRow forumRow) {
        Forum forumToRemove = forumService.getForum(forumRow.getId());
        model.addAttribute("forumName", forumToRemove.getName());

        ForumDeleteForm deleteForm = new ForumDeleteForm();
        deleteForm.setId(forumRow.getId());
        model.addAttribute("forumDeleteForm", deleteForm);

        return DELETE_VIEW_NAME;
    }

    @AdministratorPermissionRequired(CAN_DELETE_FORUMS)
    @RequestMapping(path = "/delete/confirmed", method = RequestMethod.POST)
    public String forumCategoryDeleteConfirmed(@ModelAttribute(FORUM_DELETE_FORM) ForumDeleteForm deleteForm) {
        forumService.removeForum(deleteForm.getId());

        return REDIRECT_TO_FORUM_MANAGEMENT;
    }

    private ForumCategoryRow mapToForumCategoryDto(ForumCategory categoryEntity) {
        ForumCategoryRow categoryRow = new ForumCategoryRow();
        categoryRow.setId(categoryEntity.getId());
        categoryRow.setName(categoryEntity.getName());
        return categoryRow;
    }

    private RuntimeException badCategoryId(Long id) {
        return new IllegalArgumentException("Bad category id:" + id);
    }

}
