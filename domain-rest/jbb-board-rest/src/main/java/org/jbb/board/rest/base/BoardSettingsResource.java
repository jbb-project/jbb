/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.rest.base;

import org.jbb.board.api.base.BoardException;
import org.jbb.board.api.base.BoardSettings;
import org.jbb.board.api.base.BoardSettingsService;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import javax.validation.ConstraintViolation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.board.rest.BoardRestConstants.BOARD_SETTINGS;
import static org.jbb.lib.restful.RestAuthorize.IS_AN_ADMINISTRATOR;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_BOARD_SETTINGS;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + BOARD_SETTINGS)
@RequestMapping(value = API_V1 + BOARD_SETTINGS, produces = MediaType.APPLICATION_JSON_VALUE)
public class BoardSettingsResource {

    private final BoardSettingsService boardSettingsService;

    private final BoardSettingsTranslator boardSettingsTranslator;
    private final BoardExceptionMapper boardExceptionMapper;

    @GetMapping
    @ApiOperation("Gets board settings")
    @ErrorInfoCodes({})
    public BoardSettingsDto settingsGet() {
        BoardSettings boardSettings = boardSettingsService.getBoardSettings();
        return boardSettingsTranslator.toDto(boardSettings);
    }

    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Updates board settings")
    @ErrorInfoCodes({INVALID_BOARD_SETTINGS, UNAUTHORIZED, FORBIDDEN})
    public BoardSettingsDto settingsPut(@RequestBody BoardSettingsDto boardSettingsDto) {
        boardSettingsService.setBoardSettings(boardSettingsTranslator.toModel(boardSettingsDto));
        return boardSettingsDto;
    }

    @ExceptionHandler(BoardException.class)
    ResponseEntity<ErrorResponse> handle(BoardException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(INVALID_BOARD_SETTINGS);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
                .map(boardExceptionMapper::mapToErrorDetail)
                .forEach(errorResponse.getDetails()::add);

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

}
