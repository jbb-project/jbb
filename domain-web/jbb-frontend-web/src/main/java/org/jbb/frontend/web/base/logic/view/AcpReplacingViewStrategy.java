/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.logic.view;

import com.google.common.collect.TreeMultimap;

import org.jbb.frontend.api.acp.AcpCategory;
import org.jbb.frontend.api.acp.AcpElement;
import org.jbb.frontend.api.acp.AcpService;
import org.jbb.frontend.api.acp.AcpSubcategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Component
@Order(3)
public class AcpReplacingViewStrategy extends ReplacingViewStrategy {
    private final AcpService acpService;

    @Autowired
    public AcpReplacingViewStrategy(AcpService acpService) {
        this.acpService = acpService;
    }

    @Override
    boolean canHandle(ModelAndView modelAndView) {
        return modelAndView.getViewName().startsWith("acp/");
    }

    @Override
    void performHandle(ModelAndView modelAndView) {
        String viewName = modelAndView.getViewName();

        modelAndView.getModel().put(CONTENT_VIEW_NAME, "acpLayout");
        List<AcpCategory> acpCategories = acpService.selectAllCategoriesOrdered();
        modelAndView.getModel().put("acpCategories", acpCategories);

        String[] acpNameParts = viewName.split("/"); // acp/CATEGORY_NAME/ELEMENT_NAME
        TreeMultimap<AcpSubcategory, AcpElement> subcategoryAcpElementListMultimap =
                acpService.selectAllSubcategoriesAndElements(acpNameParts[1]);
        modelAndView.getModel().put("acpSubCategoriesAndElements", subcategoryAcpElementListMultimap.asMap());

        AcpCategory currentCategory = acpService.selectCategory(acpNameParts[1]);
        modelAndView.getModel().put("currentCategory", currentCategory);

        if (acpNameParts.length == 3) {
            AcpElement currentElement = acpService.selectElement(acpNameParts[1], acpNameParts[2]);
            modelAndView.getModel().put("currentElement", currentElement);
        }

        modelAndView.getModel().put("acpContentViewName", viewName);

        modelAndView.setViewName(DEFAULT_LAYOUT_NAME);
    }
}
