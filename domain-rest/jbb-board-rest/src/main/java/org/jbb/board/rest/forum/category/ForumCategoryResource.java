/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.rest.forum.category;

import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.api.forum.ForumCategoryService;
import org.jbb.board.rest.forum.PositionDto;
import org.jbb.board.rest.forum.category.exception.TargetForumCategoryNotFound;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.board.rest.BoardRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_BOARD_READ_WRITE_SCOPE;
import static org.jbb.board.rest.BoardRestAuthorize.PERMIT_ALL_OR_OAUTH_BOARD_READ_SCOPE;
import static org.jbb.board.rest.BoardRestConstants.FORUM_CATEGORIES;
import static org.jbb.board.rest.BoardRestConstants.FORUM_CATEGORY_ID;
import static org.jbb.board.rest.BoardRestConstants.FORUM_CATEGORY_ID_VAR;
import static org.jbb.board.rest.BoardRestConstants.POSITION;
import static org.jbb.board.rest.BoardRestConstants.TARGET_FORUM_CATEGORY_PARAM;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.FORUM_CATEGORY_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_FORUM_CATEGORY;
import static org.jbb.lib.restful.domain.ErrorInfo.MISSING_PERMISSION;
import static org.jbb.lib.restful.domain.ErrorInfo.TARGET_FORUM_CATEGORY_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.TOO_LARGE_POSITION;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_ADD_FORUMS;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_DELETE_FORUMS;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_MODIFY_FORUMS;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + FORUM_CATEGORIES)
@RequestMapping(value = API_V1 + FORUM_CATEGORIES, produces = MediaType.APPLICATION_JSON_VALUE)
public class ForumCategoryResource {

    private final ForumCategoryService forumCategoryService;

    private final ForumCategoryTranslator forumCategoryTranslator;

    @GetMapping(FORUM_CATEGORY_ID)
    @ApiOperation("Gets forum category by id")
    @ErrorInfoCodes({FORUM_CATEGORY_NOT_FOUND})
    @PreAuthorize(PERMIT_ALL_OR_OAUTH_BOARD_READ_SCOPE)
    public ForumCategoryDto forumCategoryGet(@PathVariable(FORUM_CATEGORY_ID_VAR) Long forumCategoryId) {
        return forumCategoryTranslator.toDto(forumCategoryService.getCategoryChecked(forumCategoryId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_BOARD_READ_WRITE_SCOPE)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Creates forum category")
    @AdministratorPermissionRequired(CAN_ADD_FORUMS)
    @ErrorInfoCodes({INVALID_FORUM_CATEGORY, UNAUTHORIZED, FORBIDDEN, MISSING_PERMISSION})
    public ForumCategoryDto forumCategoryPost(@RequestBody CreateUpdateForumCategoryDto forumCategoryDto) {
        ForumCategory newCategory = forumCategoryService.addCategory(forumCategoryTranslator.toModel(forumCategoryDto, null));
        return forumCategoryTranslator.toDto(newCategory);
    }

    @PutMapping(value = FORUM_CATEGORY_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_BOARD_READ_WRITE_SCOPE)
    @ApiOperation("Updates forum category with id")
    @ErrorInfoCodes({INVALID_FORUM_CATEGORY, FORUM_CATEGORY_NOT_FOUND, UNAUTHORIZED, FORBIDDEN, MISSING_PERMISSION})
    @AdministratorPermissionRequired(CAN_MODIFY_FORUMS)
    public ForumCategoryDto forumCategoryPut(@PathVariable(FORUM_CATEGORY_ID_VAR) Long forumCategoryId,
                                             @RequestBody CreateUpdateForumCategoryDto forumCategoryDto) {
        ForumCategory category = forumCategoryService.getCategoryChecked(forumCategoryId);
        ForumCategory updatedCategory = forumCategoryService.editCategory(forumCategoryTranslator.toModel(forumCategoryDto, category.getId()));
        return forumCategoryTranslator.toDto(updatedCategory);
    }

    @PutMapping(value = FORUM_CATEGORY_ID + POSITION, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_BOARD_READ_WRITE_SCOPE)
    @ApiOperation("Updates position of forum category with id")
    @ErrorInfoCodes({FORUM_CATEGORY_NOT_FOUND, TOO_LARGE_POSITION, UNAUTHORIZED, FORBIDDEN, MISSING_PERMISSION})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @AdministratorPermissionRequired(CAN_MODIFY_FORUMS)
    public void forumCategoryPositionPut(@PathVariable(FORUM_CATEGORY_ID_VAR) Long forumCategoryId,
                                         @RequestBody @Validated PositionDto positionDto) {
        ForumCategory forumCategory = forumCategoryService.getCategoryChecked(forumCategoryId);
        forumCategoryService.moveCategoryToPosition(forumCategory, positionDto.getPosition() + 1);
    }

    @DeleteMapping(FORUM_CATEGORY_ID)
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_BOARD_READ_WRITE_SCOPE)
    @ApiOperation("Removes forum category with id")
    @ErrorInfoCodes({FORUM_CATEGORY_NOT_FOUND, UNAUTHORIZED, FORBIDDEN, MISSING_PERMISSION, TARGET_FORUM_CATEGORY_NOT_FOUND})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @AdministratorPermissionRequired(CAN_DELETE_FORUMS)
    public void forumCategoryDelete(@PathVariable(FORUM_CATEGORY_ID_VAR) Long forumCategoryId,
                                    @RequestParam(value = TARGET_FORUM_CATEGORY_PARAM, required = false) Long newCategoryId) {
        ForumCategory forumCategory = forumCategoryService.getCategoryChecked(forumCategoryId);
        if (newCategoryId == null) {
            forumCategoryService.removeCategoryAndForums(forumCategory.getId());
        } else {
            ForumCategory newCategory = forumCategoryService.getCategory(newCategoryId).orElseThrow(TargetForumCategoryNotFound::new);
            forumCategoryService.removeCategoryAndMoveForums(forumCategory.getId(), newCategory.getId());
        }
    }

    @ExceptionHandler(TargetForumCategoryNotFound.class)
    ResponseEntity<ErrorResponse> handle(TargetForumCategoryNotFound ex) {
        return ErrorResponse.getErrorResponseEntity(TARGET_FORUM_CATEGORY_NOT_FOUND);
    }
}
