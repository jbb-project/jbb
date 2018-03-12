/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.rest.forum;

import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumException;
import org.jbb.board.api.forum.ForumNotFoundException;
import org.jbb.board.api.forum.ForumService;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.permissions.api.annotation.AdministratorPermissionRequired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import javax.validation.ConstraintViolation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.board.rest.BoardRestConstants.FORUMS;
import static org.jbb.board.rest.BoardRestConstants.FORUM_ID;
import static org.jbb.board.rest.BoardRestConstants.FORUM_ID_VAR;
import static org.jbb.board.rest.BoardRestConstants.POSITION;
import static org.jbb.lib.restful.RestAuthorize.IS_AN_ADMINISTRATOR;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.FORUM_CATEGORY_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.FORUM_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_FORUM;
import static org.jbb.lib.restful.domain.ErrorInfo.MISSING_PERMISSION;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_DELETE_FORUMS;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_MODIFY_FORUMS;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + FORUMS)
@RequestMapping(value = API_V1 + FORUMS, produces = MediaType.APPLICATION_JSON_VALUE)
public class ForumResource {

    private final ForumService forumService;

    private final ForumTranslator forumTranslator;
    private final ForumExceptionMapper forumExceptionMapper;

    @GetMapping(FORUM_ID)
    @ApiOperation("Gets forum by id")
    @ErrorInfoCodes({FORUM_NOT_FOUND})
    public ForumDto forumGet(@PathVariable(FORUM_ID_VAR) Long forumId) throws ForumNotFoundException {
        return forumTranslator.toDto(forumService.getForumChecked(forumId));
    }

    @PutMapping(value = FORUM_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ApiOperation("Updates forum with id")
    @ErrorInfoCodes({INVALID_FORUM, FORUM_NOT_FOUND, UNAUTHORIZED, FORBIDDEN, MISSING_PERMISSION})
    @AdministratorPermissionRequired(CAN_MODIFY_FORUMS)
    public ForumDto forumPut(@PathVariable(FORUM_ID_VAR) Long forumId,
                             @RequestBody CreateUpdateForumDto forumDto) throws ForumNotFoundException {
        forumService.getForumChecked(forumId);
        Forum updatedForum = forumService.editForum(forumTranslator.toModel(forumDto));
        return forumTranslator.toDto(updatedForum);
    }

    @PutMapping(value = FORUM_ID + POSITION, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ApiOperation("Updates position of forum with id")
    @ErrorInfoCodes({FORUM_CATEGORY_NOT_FOUND, UNAUTHORIZED, FORBIDDEN, MISSING_PERMISSION})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @AdministratorPermissionRequired(CAN_MODIFY_FORUMS)
    public void forumCategoryPositionPut(@PathVariable(FORUM_ID_VAR) Long forumId,
                                         @RequestBody @Validated PositionDto positionDto) throws ForumNotFoundException {
        Forum forum = forumService.getForumChecked(forumId);
        forumService.moveForumToPosition(forum, positionDto.getPosition());
    }

    @DeleteMapping(FORUM_ID)
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ApiOperation("Removes forum with id")
    @ErrorInfoCodes({FORUM_NOT_FOUND, UNAUTHORIZED, FORBIDDEN, MISSING_PERMISSION})
    @AdministratorPermissionRequired(CAN_DELETE_FORUMS)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void forumDelete(@PathVariable(FORUM_ID_VAR) Long forumId) throws ForumNotFoundException {
        Forum forum = forumService.getForumChecked(forumId);
        forumService.removeForum(forum.getId());
    }

    @ExceptionHandler(ForumException.class)
    public ResponseEntity<ErrorResponse> handle(ForumException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(INVALID_FORUM);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
                .map(forumExceptionMapper::mapToErrorDetail)
                .forEach(errorResponse.getDetails()::add);

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}
