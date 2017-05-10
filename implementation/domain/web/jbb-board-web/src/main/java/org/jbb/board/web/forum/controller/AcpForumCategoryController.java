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
import org.jbb.board.web.forum.form.ForumCategoryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/acp/general/forums/category")
public class AcpForumCategoryController {
    private static final String VIEW_NAME = "acp/general/forumcategory";
    private static final String CATEGORY_FORM = "forumCategoryForm";
    private static final String FORM_SAVED_FLAG = "forumCategoryFormSaved";

    private final BoardService boardService;

    @Autowired
    public AcpForumCategoryController(BoardService boardService) {
        this.boardService = boardService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String forumCategoryGet(@RequestParam(value = "id", required = false) Long categoryId, Model model) {
        ForumCategoryForm form = new ForumCategoryForm();
        if (categoryId != null) {
            ForumCategory category = boardService.getCategory(categoryId);
            if (category != null) {
                form.setId(category.getId());
                form.setName(category.getName());
            }
        }
        model.addAttribute(CATEGORY_FORM, form);
        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String forumCategoryPost(Model model,
                                    @ModelAttribute(CATEGORY_FORM) ForumCategoryForm form,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.debug("Forum category form error detected: {}", bindingResult.getAllErrors());
            model.addAttribute(FORM_SAVED_FLAG, false);
            return VIEW_NAME;
        }

        try {
            if (form.getId() != null) {
                ForumCategory categoryEntity = boardService.getCategory(form.getId());

                ForumCategory updatedForumCategory = form.getForumCategory(categoryEntity.getForums());
                boardService.editCategory(updatedForumCategory);

                model.addAttribute(FORM_SAVED_FLAG, true);
                return VIEW_NAME;
            } else {
                ForumCategory newForumCategory = form.getForumCategory(Lists.newArrayList());
                boardService.addCategory(newForumCategory);

                redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
                return "redirect:/acp/general/forums";
            }
        } catch (BoardException e) {
            log.debug("Error during add/update forum category: {}", e);
            model.addAttribute(FORM_SAVED_FLAG, false);
            return VIEW_NAME;
        }
    }
}
