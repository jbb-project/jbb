/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.web.base.controller;

import com.google.common.collect.Sets;

import org.jbb.board.api.base.BoardSettings;
import org.jbb.board.api.base.BoardSettingsService;
import org.jbb.board.web.base.form.BoardSettingsForm;
import org.jbb.board.web.base.logic.AcpBoardSettingsErrorMapper;
import org.jbb.frontend.api.format.FormatSettings;
import org.jbb.frontend.api.format.FormatSettingsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/general/board")
public class AcpBoardSettingsController {
    private static final String VIEW_NAME = "acp/general/board";
    private static final String GENERAL_BOARD_FORM = "generalBoardForm";
    private static final String FORM_SAVED_FLAG = "generalBoardFormSaved";

    private final BoardSettingsService boardSettingsService;
    private final FormatSettingsService formatSettingsService;

    private final Validator validator;
    private final AcpBoardSettingsErrorMapper errorMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String generalBoardGet(Model model,
                                  @ModelAttribute(GENERAL_BOARD_FORM) BoardSettingsForm form) {
        BoardSettings boardSettings = boardSettingsService.getBoardSettings();
        form.setBoardName(boardSettings.getBoardName());

        FormatSettings formatSettings = formatSettingsService.getFormatSettings();
        form.setDateFormat(formatSettings.getDateFormat());
        form.setDurationFormat(formatSettings.getDurationFormat());

        model.addAttribute(GENERAL_BOARD_FORM, form);
        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String generalBoardPost(@ModelAttribute(GENERAL_BOARD_FORM) BoardSettingsForm form,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        BoardSettings newBoardSettings = BoardSettings.builder()
                .boardName(form.getBoardName())
                .build();

        FormatSettings newFormatSettings = FormatSettings.builder()
                .dateFormat(form.getDateFormat())
                .durationFormat(form.getDurationFormat())
                .build();

        Set<ConstraintViolation<?>> result = Sets.newHashSet();
        result.addAll(validator.validate(newBoardSettings));
        result.addAll(validator.validate(newFormatSettings));

        if (!result.isEmpty()) {
            errorMapper.map(result, bindingResult);
            redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, false);
            return VIEW_NAME;
        }

        boardSettingsService.setBoardSettings(newBoardSettings);
        formatSettingsService.setFormatSettings(newFormatSettings);

        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        return "redirect:/" + VIEW_NAME;
    }
}
