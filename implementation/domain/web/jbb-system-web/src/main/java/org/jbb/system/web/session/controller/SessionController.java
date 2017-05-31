/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.session.controller;

import org.jbb.system.api.model.session.UserSession;
import org.jbb.system.api.service.SessionService;
import org.jbb.system.web.session.data.SessionUITableRow;
import org.jbb.system.web.session.form.InactiveIntervalTimeForm;
import org.jbb.system.web.session.form.SessionRemoveForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
@RequestMapping("/acp/system/sessions")
public class SessionController {

    private static final String VIEW_NAME = "acp/system/sessions";
    private static final String REDIRECT_TO_SESSION_VIEW = "redirect:/" + VIEW_NAME;
    private static final String USER_SESSION_MODEL_ATTRIBUTE = "userSessions";
    private static final String SESSION_FORM_NAME = "sessionSettingsForm";
    private static final String SESSION_REMOVE_FORM_NAME = "sessionRemoveForm";
    private static final String MAX_INACTIVE_INTERVAL_TIME_FLASH_ATTRIBUTE = "savecorrectly";

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService){
        this.sessionService = sessionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getSessionView(Model model){

        InactiveIntervalTimeForm inactiveIntervalTimeForm = new InactiveIntervalTimeForm();

        inactiveIntervalTimeForm.setMaxInactiveIntervalTime(sessionService.getMaxInactiveSessionInterval().getSeconds());
        List<UserSession> userSessionList = sessionService.getAllUserSessions();

        List<SessionUITableRow> sessionUITableRowList = userSessionList.stream()
                .map(userSession ->
                        new SessionUITableRow(userSession.sessionId(),
                                        userSession.creationTime(),
                                        userSession.lastAccessedTime(),
                                        userSession.usedTime(),
                                        userSession.inactiveTime(),
                                        userSession.userName(),
                                        userSession.displayUserName(),
                                        userSession.timeToLive()))
                .collect(Collectors.toList());

        model.addAttribute(USER_SESSION_MODEL_ATTRIBUTE, sessionUITableRowList);

        if (!model.containsAttribute(SESSION_FORM_NAME)) {
            model.addAttribute(SESSION_FORM_NAME, inactiveIntervalTimeForm);
        }
        return VIEW_NAME;
    }

    @RequestMapping(value = "/properties",method = RequestMethod.POST)
    public String saveNewValueOfMaxInActiveIntervalTimeAttribute(@ModelAttribute(SESSION_FORM_NAME) @Valid InactiveIntervalTimeForm inactiveIntervalTimeForm,
                                                                 BindingResult bindingResult,
                                                                 RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            log.debug("Binding result exceptions are: {}", bindingResult.getFieldErrors());
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + SESSION_FORM_NAME, bindingResult);
            redirectAttributes.addFlashAttribute(SESSION_FORM_NAME, inactiveIntervalTimeForm);
            return REDIRECT_TO_SESSION_VIEW;

        }
        sessionService.setMaxInactiveSessionInterval(Duration.ofSeconds(inactiveIntervalTimeForm.getMaxInactiveIntervalTime()));
        redirectAttributes.addFlashAttribute(MAX_INACTIVE_INTERVAL_TIME_FLASH_ATTRIBUTE, true);
        return REDIRECT_TO_SESSION_VIEW;
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String removeSession(@ModelAttribute(SESSION_REMOVE_FORM_NAME) @Valid SessionRemoveForm form) {

        sessionService.terminateSession(form.getId());
        return REDIRECT_TO_SESSION_VIEW;
    }
}
