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

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.board.api.forum.BoardService;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.api.forum.ForumCategoryException;
import org.jbb.board.api.forum.ForumCategoryService;
import org.jbb.board.web.forum.data.ForumCategoryRow;
import org.jbb.board.web.forum.form.ForumCategoryDeleteForm;
import org.jbb.board.web.forum.form.ForumCategoryForm;
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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/general/forums/category")
public class AcpForumCategoryController {
    private static final String VIEW_NAME = "acp/general/forumcategory";
    private static final String DELETE_VIEW_NAME = "acp/general/forumcategory-delete";
    private static final String REDIRECT_TO_FORUM_MANAGEMENT = "redirect:/acp/general/forums";

    private static final String CATEGORY_FORM = "forumCategoryForm";
    private static final String CATEGORY_DELETE_FORM = "forumCategoryDeleteForm";
    private static final String CATEGORY_ROW = "forumCategory";
    private static final String EDIT_POSSIBLE = "hasPermissionToEdit";

    private final BoardService boardService;
    private final ForumCategoryService forumCategoryService;
    private final PermissionService permissionService;
    private final SimpleErrorsBindingMapper errorMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String forumCategoryGet(@RequestParam(value = "id", required = false) Long categoryId, Model model) {
        ForumCategoryForm form = new ForumCategoryForm();
        if (categoryId == null) {
            model.addAttribute(EDIT_POSSIBLE, permissionService.checkPermission(CAN_ADD_FORUMS));
        } else {
            model.addAttribute(EDIT_POSSIBLE, permissionService.checkPermission(CAN_MODIFY_FORUMS));
            Optional<ForumCategory> category = forumCategoryService.getCategory(categoryId);
            if (category.isPresent()) {
                form.setId(category.get().getId());
                form.setName(category.get().getName());
            }
        }
        model.addAttribute(CATEGORY_FORM, form);
        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String forumCategoryPost(Model model,
                                    @ModelAttribute(CATEGORY_FORM) ForumCategoryForm form, BindingResult bindingResult) {

        boolean updateMode = form.getId() != null;
        try {
            if (updateMode) {
                permissionService.assertPermission(CAN_MODIFY_FORUMS);
                Optional<ForumCategory> categoryEntity = forumCategoryService.getCategory(form.getId());

                ForumCategory updatedForumCategory = form.getForumCategory(
                        categoryEntity.orElseThrow(() -> badCategoryId(form.getId())).getForums()
                );
                forumCategoryService.editCategory(updatedForumCategory);
            } else {
                permissionService.assertPermission(CAN_ADD_FORUMS);
                ForumCategory newForumCategory = form.getForumCategory(Lists.newArrayList());
                forumCategoryService.addCategory(newForumCategory);
            }
        } catch (ForumCategoryException e) {
            log.debug("Error during add/update forum category: {}", e);
            errorMapper.map(e.getConstraintViolations(), bindingResult);
            model.addAttribute(EDIT_POSSIBLE, updateMode ?
                    permissionService.checkPermission(CAN_MODIFY_FORUMS) :
                    permissionService.checkPermission(CAN_ADD_FORUMS));
            return VIEW_NAME;
        }

        return REDIRECT_TO_FORUM_MANAGEMENT;
    }

    @AdministratorPermissionRequired(CAN_MODIFY_FORUMS)
    @RequestMapping(path = "/moveup", method = RequestMethod.POST)
    public String forumCategoryMoveUpPost(@ModelAttribute(CATEGORY_ROW) ForumCategoryRow categoryRow) {
        Optional<ForumCategory> categoryEntity = forumCategoryService.getCategory(categoryRow.getId());
        forumCategoryService.moveCategoryToPosition(
                categoryEntity.orElseThrow(() -> badCategoryId(categoryRow.getId())), categoryRow.getPosition() - 1);
        return REDIRECT_TO_FORUM_MANAGEMENT;
    }

    @AdministratorPermissionRequired(CAN_MODIFY_FORUMS)
    @RequestMapping(path = "/movedown", method = RequestMethod.POST)
    public String forumCategoryMoveDownPost(@ModelAttribute(CATEGORY_ROW) ForumCategoryRow categoryRow) {
        Optional<ForumCategory> categoryEntity = forumCategoryService.getCategory(categoryRow.getId());
        forumCategoryService.moveCategoryToPosition(
                categoryEntity.orElseThrow(() -> badCategoryId(categoryRow.getId())), categoryRow.getPosition() + 1);
        return REDIRECT_TO_FORUM_MANAGEMENT;
    }

    @AdministratorPermissionRequired(CAN_DELETE_FORUMS)
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String forumCategoryDelete(Model model, @ModelAttribute(CATEGORY_ROW) ForumCategoryRow categoryRow) {
        Optional<ForumCategory> categoryEntity = forumCategoryService.getCategory(categoryRow.getId());
        model.addAttribute("forumCategoryName",
                categoryEntity.orElseThrow(() -> badCategoryId(categoryRow.getId())).getName());

        List<ForumCategory> allCategories = boardService.getForumCategories();
        List<ForumCategoryRow> categoryDtos = allCategories.stream()
                .filter(category -> !category.getId().equals(categoryEntity.get().getId()))
                .map(this::mapToForumCategoryDto)
                .collect(Collectors.toList());
        model.addAttribute("availableCategories", categoryDtos);

        ForumCategoryDeleteForm deleteForm = new ForumCategoryDeleteForm();
        deleteForm.setRemoveWithForums(true);
        deleteForm.setId(categoryRow.getId());
        model.addAttribute(CATEGORY_DELETE_FORM, deleteForm);

        return DELETE_VIEW_NAME;
    }

    @AdministratorPermissionRequired(CAN_DELETE_FORUMS)
    @RequestMapping(path = "/delete/confirmed", method = RequestMethod.POST)
    public String forumCategoryDeleteConfirmed(@ModelAttribute(CATEGORY_DELETE_FORM) ForumCategoryDeleteForm deleteForm) {

        if (deleteForm.getRemoveWithForums()) {
            forumCategoryService.removeCategoryAndForums(deleteForm.getId());
        } else {
            forumCategoryService.removeCategoryAndMoveForums(deleteForm.getId(), deleteForm.getNewCategoryId());
        }

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
