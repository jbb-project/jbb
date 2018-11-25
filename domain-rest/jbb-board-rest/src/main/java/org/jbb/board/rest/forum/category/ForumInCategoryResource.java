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

import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.board.api.forum.ForumCategoryService;
import org.jbb.board.api.forum.ForumService;
import org.jbb.board.rest.forum.CreateUpdateForumDto;
import org.jbb.board.rest.forum.ForumDto;
import org.jbb.board.rest.forum.ForumTranslator;
import org.jbb.board.rest.forum.ForumsDto;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.permissions.api.annotation.AdministratorPermissionRequired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.board.rest.BoardRestConstants.FORUMS;
import static org.jbb.board.rest.BoardRestConstants.FORUM_CATEGORIES;
import static org.jbb.board.rest.BoardRestConstants.FORUM_CATEGORY_ID;
import static org.jbb.board.rest.BoardRestConstants.FORUM_CATEGORY_ID_VAR;
import static org.jbb.board.rest.BoardRestConstants.FORUM_ID;
import static org.jbb.board.rest.BoardRestConstants.FORUM_ID_VAR;
import static org.jbb.lib.restful.RestAuthorize.IS_AN_ADMINISTRATOR;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.FORUM_CATEGORY_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.FORUM_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_FORUM;
import static org.jbb.lib.restful.domain.ErrorInfo.MISSING_PERMISSION;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_ADD_FORUMS;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_MODIFY_FORUMS;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + FORUM_CATEGORIES + FORUM_CATEGORY_ID + FORUMS)
@RequestMapping(value = API_V1 + FORUM_CATEGORIES + FORUM_CATEGORY_ID + FORUMS, produces = MediaType.APPLICATION_JSON_VALUE)
public class ForumInCategoryResource {

    private final ForumCategoryService forumCategoryService;
    private final ForumService forumService;

    private final ForumTranslator forumTranslator;

    @GetMapping
    @ApiOperation("Gets forums in category")
    @ErrorInfoCodes({FORUM_CATEGORY_NOT_FOUND})
    public ForumsDto forumsGet(@PathVariable(FORUM_CATEGORY_ID_VAR) Long forumCategoryId) {
        ForumCategory category = forumCategoryService.getCategoryChecked(forumCategoryId);
        return forumTranslator.toDto(category.getForums());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Creates forum in category")
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @AdministratorPermissionRequired(CAN_ADD_FORUMS)
    @ErrorInfoCodes({INVALID_FORUM, FORUM_CATEGORY_NOT_FOUND, UNAUTHORIZED, FORBIDDEN, MISSING_PERMISSION})
    @ResponseStatus(HttpStatus.CREATED)
    public ForumDto forumPost(@PathVariable(FORUM_CATEGORY_ID_VAR) Long forumCategoryId,
                              @RequestBody CreateUpdateForumDto forumDto) {
        ForumCategory category = forumCategoryService.getCategoryChecked(forumCategoryId);
        Forum forum = forumTranslator.toModel(forumDto, null);
        Forum createdForum = forumService.addForum(forum, category);
        return forumTranslator.toDto(createdForum);
    }

    @PutMapping(FORUM_ID)
    @ApiOperation("Moves forum to the given category")
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @AdministratorPermissionRequired(CAN_MODIFY_FORUMS)
    @ErrorInfoCodes({FORUM_CATEGORY_NOT_FOUND, FORUM_NOT_FOUND, UNAUTHORIZED, FORBIDDEN, MISSING_PERMISSION})
    public ForumDto forumMovePut(@PathVariable(FORUM_CATEGORY_ID_VAR) Long forumCategoryId,
                                 @PathVariable(FORUM_ID_VAR) Long forumId) {
        ForumCategory category = forumCategoryService.getCategoryChecked(forumCategoryId);
        Forum forum = forumService.getForumChecked(forumId);
        Forum updatedForum = forumService.moveForumToAnotherCategory(forum.getId(), category.getId());
        return forumTranslator.toDto(updatedForum);
    }

}
