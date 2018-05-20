/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.rest.post;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.DELETE_POST_NOT_POSSIBLE;
import static org.jbb.lib.restful.domain.ErrorInfo.MEMBER_POST_WITH_ANON_NAME;
import static org.jbb.lib.restful.domain.ErrorInfo.POST_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.UPDATE_POST_NOT_POSSIBLE;
import static org.jbb.posting.rest.PostingRestConstants.CONTENT;
import static org.jbb.posting.rest.PostingRestConstants.POSTS;
import static org.jbb.posting.rest.PostingRestConstants.POST_ID;
import static org.jbb.posting.rest.PostingRestConstants.POST_ID_VAR;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.posting.api.PostingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + POSTS)
@RequestMapping(value = API_V1 + POSTS, produces = MediaType.APPLICATION_JSON_VALUE)
public class PostResource {

    private final PostingService postingService;

    @GetMapping(POST_ID)
    @ErrorInfoCodes({POST_NOT_FOUND})
    @ApiOperation("Gets post by id")
    public PostDto postGet(@PathVariable(POST_ID_VAR) Long postId) {
        return PostDto.builder().build();
    }

    @GetMapping(POST_ID + CONTENT)
    @ErrorInfoCodes({POST_NOT_FOUND})
    @ApiOperation("Gets post with content by id")
    public PostContentDto postContentGet(@PathVariable(POST_ID_VAR) Long postId) {
        return PostContentDto.builder().build();
    }

    @PutMapping(value = POST_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ErrorInfoCodes({POST_NOT_FOUND, MEMBER_POST_WITH_ANON_NAME, UPDATE_POST_NOT_POSSIBLE})
    @ApiOperation("Updates post by id")
    public PostDto postUpdate(@PathVariable(POST_ID_VAR) Long postId,
        @Validated @RequestBody CreateUpdatePostDto createUpdatePost) {
        return PostDto.builder().build();
    }

    @DeleteMapping(POST_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Removes post by id")
    @ErrorInfoCodes({POST_NOT_FOUND, DELETE_POST_NOT_POSSIBLE})
    public void postDelete(@PathVariable(POST_ID_VAR) Long postId) {

    }

}
