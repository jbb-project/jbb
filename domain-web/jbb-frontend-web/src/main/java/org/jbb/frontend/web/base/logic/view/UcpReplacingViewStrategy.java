/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.logic.view;

import org.jbb.frontend.api.ucp.UcpCategory;
import org.jbb.frontend.api.ucp.UcpElement;
import org.jbb.frontend.api.ucp.UcpService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
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
        List<UcpCategory> ucpCategories = ucpService.selectAllCategoriesOrdered();
        modelAndView.getModel().put("ucpCategories", ucpCategories);

        String[] ucpNameParts = viewName.split("/");
        List<UcpElement> ucpElements = ucpService.selectAllElementsOrderedForCategoryViewName(ucpNameParts[1]);
        modelAndView.getModel().put("ucpElements", ucpElements);

        UcpCategory currentCategory = ucpService.selectCategoryForViewName(ucpNameParts[1]);
        modelAndView.getModel().put("currentCategory", currentCategory);

        UcpElement currentElement = ucpService.selectElementForViewName(ucpNameParts[1], ucpNameParts[2]);
        modelAndView.getModel().put("currentElement", currentElement);

        modelAndView.getModel().put("ucpContentViewName", viewName);

        modelAndView.setViewName(DEFAULT_LAYOUT_NAME);
    }
}
