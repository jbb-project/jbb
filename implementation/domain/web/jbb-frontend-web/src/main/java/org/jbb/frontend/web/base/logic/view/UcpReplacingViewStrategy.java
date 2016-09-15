/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.logic.view;

import org.jbb.frontend.api.model.UcpCategory;
import org.jbb.frontend.api.model.UcpElement;
import org.jbb.frontend.api.service.UcpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Component
@Order(2)
public class UcpReplacingViewStrategy extends ReplacingViewStrategy {
    private final UcpService ucpService;

    @Autowired
    public UcpReplacingViewStrategy(UcpService ucpService) {
        this.ucpService = ucpService;
    }

    @Override
    boolean canHandle(ModelAndView modelAndView) {
        return modelAndView.getViewName().startsWith("ucp/");
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

        UcpCategory currentCategory = ucpService.selectForViewName(ucpNameParts[1]);
        modelAndView.getModel().put("currentCategory", currentCategory);

        UcpElement currentElement = ucpService.selectElementForViewName(ucpNameParts[1], ucpNameParts[2]);
        modelAndView.getModel().put("currentElement", currentElement);

        modelAndView.getModel().put("ucpContentViewName", viewName);

        modelAndView.setViewName(DEFAULT_LAYOUT_NAME);
    }
}
