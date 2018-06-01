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

import com.google.common.collect.Lists;

import org.apache.commons.collections.CollectionUtils;
import org.jbb.board.api.forum.BoardService;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.board.rest.BoardRestConstants.BOARD_VIEW;
import static org.jbb.lib.restful.RestConstants.API_V1;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + BOARD_VIEW)
@RequestMapping(value = API_V1 + BOARD_VIEW, produces = MediaType.APPLICATION_JSON_VALUE)
public class BoardResource {

    private final BoardService boardService;

    private final BoardTranslator boardTranslator;

    @GetMapping
    @ErrorInfoCodes({})
    @ApiOperation("Gets board structure with forum details")
    public BoardDto boardGet(@RequestParam(required = false) List<ForumViewParams> forumDetails) {
        List<ForumViewParams> targetForumViewDetails;
        if (CollectionUtils.isEmpty(forumDetails)) {
            targetForumViewDetails = Lists.newArrayList();
        } else {
            targetForumViewDetails = forumDetails;
            targetForumViewDetails.remove(null);
        }
        return boardTranslator.toDto(boardService.getForumCategories(), targetForumViewDetails);
    }
}
