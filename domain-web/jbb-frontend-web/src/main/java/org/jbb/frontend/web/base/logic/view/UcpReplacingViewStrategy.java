/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.logic.view;

import org.jbb.frontend.api.ucp.UcpService;
import org.jbb.frontend.api.ucp.UcpStructure;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Component
@Order(3)
@RequiredArgsConstructor
public class UcpReplacingViewStrategy extends ReplacingViewStrategy {
    private final UcpService ucpService;

    @Override
    boolean canHandle(ModelAndView modelAndView) {
        return Optional.ofNullable(modelAndView.getViewName())
                .map(viewName -> viewName.startsWith("ucp/"))
                .orElse(false);
    }

    @Override
    void performHandle(ModelAndView modelAndView) {
        String viewName = modelAndView.getViewName();

        modelAndView.getModel().put(CONTENT_VIEW_NAME, "ucpLayout");
        UcpStructure ucpStructure = ucpService.getUcpStructure();
        modelAndView.getModel().put("ucpCategories", ucpStructure.getCategories());

        String[] ucpNameParts = viewName.split("/"); // ucp/CATEGORY_NAME/ELEMENT_NAME
        UcpStructure.Category currentCategory = ucpStructure.findCategoryByViewName(ucpNameParts[1]);
        modelAndView.getModel().put("currentCategory", currentCategory);
        modelAndView.getModel().put("ucpElements", currentCategory.getElements());

        UcpStructure.Element currentElement = currentCategory.findElementByViewName(ucpNameParts[2]);
        modelAndView.getModel().put("currentElement", currentElement);

        modelAndView.getModel().put("ucpContentViewName", viewName);

        modelAndView.setViewName(DEFAULT_LAYOUT_NAME);
    }
}
