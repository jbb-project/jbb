/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.controller;

import org.jbb.members.web.registration.controller.RegisterController;
import org.jbb.members.web.registration.form.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/acp/members/create")
public class AcpCreateMemberController {
    private static final String VIEW_NAME = "acp/members/create";
    private static final String REGISTER_FORM = "registerForm";

    private final RegisterController registerController;

    @Autowired
    public AcpCreateMemberController(RegisterController registerController) {
        this.registerController = registerController;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String memberCreateGet(Model model) {
        registerController.signUp(model, null);
        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String memberCreatePost(Model model,
                                   @ModelAttribute(REGISTER_FORM) RegisterForm registerForm,
                                   BindingResult result, HttpServletRequest httpServletRequest,
                                   RedirectAttributes redirectAttributes) {
        registerController.processRegisterForm(model, registerForm,
                result, httpServletRequest, redirectAttributes);
        return VIEW_NAME;
    }
}
