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

import com.google.common.collect.Lists;

import org.jbb.board.api.exception.BoardException;
import org.jbb.board.api.model.ForumCategory;
import org.jbb.board.api.service.BoardService;
import org.jbb.board.api.service.ForumCategoryService;
import org.jbb.board.web.forum.data.ForumCategoryRow;
import org.jbb.board.web.forum.form.ForumCategoryDeleteForm;
import org.jbb.board.web.forum.form.ForumCategoryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/acp/general/forums/category")
public class AcpForumCategoryController {
    private static final String VIEW_NAME = "acp/general/forumcategory";
    private static final String REDIRECT_TO_FORUM_MANAGEMENT = "redirect:/acp/general/forums";

    private static final String CATEGORY_FORM = "forumCategoryForm";
    private static final String CATEGORY_DELETE_FORM = "forumCategoryDeleteForm";
    private static final String CATEGORY_ROW = "forumCategory";

    private final BoardService boardService;
    private final ForumCategoryService forumCategoryService;

    @Autowired
    public AcpForumCategoryController(BoardService boardService,
                                      ForumCategoryService forumCategoryService) {
        this.boardService = boardService;
        this.forumCategoryService = forumCategoryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String forumCategoryGet(@RequestParam(value = "id", required = false) Long categoryId, Model model) {
        ForumCategoryForm form = new ForumCategoryForm();
        if (categoryId != null) {
            ForumCategory category = forumCategoryService.getCategory(categoryId);
            if (category != null) {
                form.setId(category.getId());
                form.setName(category.getName());
            }
        }
        model.addAttribute(CATEGORY_FORM, form);
        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String forumCategoryPost(@ModelAttribute(CATEGORY_FORM) ForumCategoryForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.debug("Forum category form error detected: {}", bindingResult.getAllErrors());
            return VIEW_NAME;
        }

        try {
            if (form.getId() != null) {
                ForumCategory categoryEntity = forumCategoryService.getCategory(form.getId());

                ForumCategory updatedForumCategory = form.getForumCategory(categoryEntity.getForums());
                forumCategoryService.editCategory(updatedForumCategory);
            } else {
                ForumCategory newForumCategory = form.getForumCategory(Lists.newArrayList());
                forumCategoryService.addCategory(newForumCategory);
            }
        } catch (BoardException e) {
            log.debug("Error during add/update forum category: {}", e);
            return VIEW_NAME;
        }

        return REDIRECT_TO_FORUM_MANAGEMENT;
    }

    @RequestMapping(path = "/moveup", method = RequestMethod.POST)
    public String forumCategoryMoveUpPost(@ModelAttribute(CATEGORY_ROW) ForumCategoryRow categoryRow) {
        ForumCategory categoryEntity = forumCategoryService.getCategory(categoryRow.getId());
        forumCategoryService.moveCategoryToPosition(categoryEntity, categoryRow.getPosition() - 1);
        return REDIRECT_TO_FORUM_MANAGEMENT;
    }

    @RequestMapping(path = "/movedown", method = RequestMethod.POST)
    public String forumCategoryMoveDownPost(@ModelAttribute(CATEGORY_ROW) ForumCategoryRow categoryRow) {
        ForumCategory categoryEntity = forumCategoryService.getCategory(categoryRow.getId());
        forumCategoryService.moveCategoryToPosition(categoryEntity, categoryRow.getPosition() + 1);
        return REDIRECT_TO_FORUM_MANAGEMENT;
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String forumCategoryDelete(Model model, @ModelAttribute(CATEGORY_ROW) ForumCategoryRow categoryRow) {
        ForumCategory categoryEntity = forumCategoryService.getCategory(categoryRow.getId());
        model.addAttribute("forumCategoryName", categoryEntity.getName());

        List<ForumCategory> allCategories = boardService.getForumCategories();
        List<ForumCategoryRow> categoryDtos = allCategories.stream()
                .filter(category -> !category.getId().equals(categoryEntity.getId()))
                .map(category -> mapToForumCategoryDto(category))
                .collect(Collectors.toList());
        model.addAttribute("availableCategories", categoryDtos);

        ForumCategoryDeleteForm deleteForm = new ForumCategoryDeleteForm();
        deleteForm.setRemoveWithForums(true);
        deleteForm.setId(categoryRow.getId());
        model.addAttribute("forumCategoryDeleteForm", deleteForm);

        return "acp/general/forumcategory-delete";
    }

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
}
