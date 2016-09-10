/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.interceptor;

import org.jbb.frontend.api.model.UcpCategory;
import org.jbb.frontend.api.model.UcpElement;
import org.jbb.frontend.api.service.UcpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ReplacingViewInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UcpService ucpService;

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String viewName = modelAndView.getViewName();
        if (viewName.startsWith("redirect:")) {
            return;
        } else if (viewName.startsWith("ucp/")) {
            modelAndView.getModel().put("contentViewName", "ucpLayout");
            List<UcpCategory> ucpCategories = ucpService.selectAllCategoriesOrdered();
            modelAndView.getModel().put("ucpCategories", ucpCategories);

            String[] ucpNameParts = viewName.split("/");
            List<UcpElement> ucpElements = ucpService.selectAllElementsOrderedForCategoryViewName(ucpNameParts[1]);
            modelAndView.getModel().put("ucpElements", ucpElements);

            modelAndView.getModel().put("currentCategory", ucpService.selectForViewName(ucpNameParts[1]));
            modelAndView.getModel().put("ucpContentViewName", viewName);


        } else {
            modelAndView.getModel().put("contentViewName", viewName);
        }
        modelAndView.setViewName("defaultLayout");
    }
}
