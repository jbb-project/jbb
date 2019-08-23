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

import org.jbb.frontend.api.acp.AcpService;
import org.jbb.frontend.api.acp.AcpStructure;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Component
@Order(4)
@RequiredArgsConstructor
public class AcpReplacingViewStrategy extends ReplacingViewStrategy {
    private final AcpService acpService;

    @Override
    boolean canHandle(ModelAndView modelAndView) {
        return Optional.ofNullable(modelAndView.getViewName())
                .map(viewName -> viewName.startsWith("acp/"))
                .orElse(false);
    }

    @Override
    void performHandle(ModelAndView modelAndView) {
        String viewName = modelAndView.getViewName();

        modelAndView.getModel().put(CONTENT_VIEW_NAME, "acpLayout");
        AcpStructure acpStructure = acpService.getAcpStructure();
        modelAndView.getModel().put("acpCategories", acpStructure.getCategories());

        String[] acpNameParts = viewName.split("/"); // acp/CATEGORY_NAME/ELEMENT_NAME

        AcpStructure.Category currentCategory = acpStructure.findCategoryByViewName(acpNameParts[1]);
        modelAndView.getModel().put("currentCategory", currentCategory);

        if (acpNameParts.length == 3) {
            AcpStructure.Element currentElement = currentCategory.findElementByViewName(acpNameParts[2]);
            modelAndView.getModel().put("currentElement", currentElement);
        }

        modelAndView.getModel().put("acpContentViewName", viewName);

        modelAndView.setViewName(DEFAULT_LAYOUT_NAME);
    }
}
