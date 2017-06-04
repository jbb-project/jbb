/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.web.base.controller;

import org.jbb.board.api.exception.BoardException;
import org.jbb.board.api.model.BoardSettings;
import org.jbb.board.api.service.BoardSettingsService;
import org.jbb.board.web.base.form.BoardSettingsForm;
import org.jbb.board.web.base.logic.AcpBoardSettingsErrorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/acp/general/board")
public class AcpBoardSettingsController {
    private static final String VIEW_NAME = "acp/general/board";
    private static final String GENERAL_BOARD_FORM = "generalBoardForm";
    private static final String FORM_SAVED_FLAG = "generalBoardFormSaved";

    private final BoardSettingsService boardSettingsService;
    private final AcpBoardSettingsErrorMapper errorMapper;

    @Autowired
    public AcpBoardSettingsController(BoardSettingsService boardSettingsService,
                                      AcpBoardSettingsErrorMapper errorMapper) {
        this.boardSettingsService = boardSettingsService;
        this.errorMapper = errorMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String generalBoardGet(Model model,
                                  @ModelAttribute(GENERAL_BOARD_FORM) BoardSettingsForm form) {
        BoardSettings boardSettings = boardSettingsService.getBoardSettings();
        form.setBoardName(boardSettings.getBoardName());
        form.setDateFormat(boardSettings.getDateFormat());
        model.addAttribute(GENERAL_BOARD_FORM, form);
        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String generalBoardPost(@ModelAttribute(GENERAL_BOARD_FORM) BoardSettingsForm form,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        BoardSettings newBoardSettings = new BoardSettings() {
            @Override
            public String getBoardName() {
                return form.getBoardName();
            }

            @Override
            public String getDateFormat() {
                return form.getDateFormat();
            }
        };

        try {
            boardSettingsService.setBoardSettings(newBoardSettings);
        } catch (BoardException e) {
            log.debug("Exception during setting new board settings", e);
            errorMapper.map(e.getConstraintViolations(), bindingResult);
            redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, false);
            return VIEW_NAME;
        }
        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        return "redirect:/" + VIEW_NAME;
    }
}
