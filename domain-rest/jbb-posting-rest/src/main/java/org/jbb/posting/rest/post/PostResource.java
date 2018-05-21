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
import static org.jbb.lib.restful.domain.ErrorInfo.MEMBER_FILLED_ANON_NAME;
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
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.posting.api.PostingService;
import org.jbb.posting.api.base.FullPost;
import org.jbb.posting.api.base.Post;
import org.jbb.posting.api.exception.PostNotFoundException;
import org.jbb.posting.rest.post.exception.DeletePostNotPossible;
import org.jbb.posting.rest.post.exception.MemberFilledAnonymousName;
import org.jbb.posting.rest.post.exception.UpdatePostNotPossible;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + POSTS)
@RequestMapping(value = API_V1 + POSTS, produces = MediaType.APPLICATION_JSON_VALUE)
public class PostResource {

    private final PostingService postingService;

    private final PostDtoTranslator postTranslator;
    private final PostModelTranslator postModelTranslator;

    @GetMapping(POST_ID)
    @ErrorInfoCodes({POST_NOT_FOUND})
    @ApiOperation("Gets post by id")
    public PostDto getPost(@PathVariable(POST_ID_VAR) Long postId) throws PostNotFoundException {
        Post post = postingService.getPost(postId);
        return postTranslator.toDto(post);
    }

    @GetMapping(POST_ID + CONTENT)
    @ErrorInfoCodes({POST_NOT_FOUND})
    @ApiOperation("Gets post with content by id")
    public PostContentDto getPostContent(@PathVariable(POST_ID_VAR) Long postId)
        throws PostNotFoundException {
        FullPost post = postingService.getFullPost(postId);
        return postTranslator.toContentDto(post);
    }

    @PutMapping(value = POST_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ErrorInfoCodes({POST_NOT_FOUND, MEMBER_FILLED_ANON_NAME, UPDATE_POST_NOT_POSSIBLE})
    @ApiOperation("Updates post by id")
    public PostDto postUpdate(@PathVariable(POST_ID_VAR) Long postId,
        @Validated @RequestBody CreateUpdatePostDto createUpdatePost) throws PostNotFoundException {
        Post post = postingService.getPost(postId);
        Post updatedPost = postingService
            .editPost(post.getId(), postModelTranslator.toEditPostModel(createUpdatePost, post));
        return postTranslator.toDto(updatedPost);
    }

    @DeleteMapping(POST_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Removes post by id")
    @ErrorInfoCodes({POST_NOT_FOUND, DELETE_POST_NOT_POSSIBLE})
    public void postDelete(@PathVariable(POST_ID_VAR) Long postId) throws PostNotFoundException {
        Post post = postingService.getPost(postId);
        postModelTranslator.assertDeletePostPrivileges();
        postingService.removePost(post.getId());
    }

    @ExceptionHandler(DeletePostNotPossible.class)
    ResponseEntity<ErrorResponse> handle(DeletePostNotPossible ex) {
        return ErrorResponse.getErrorResponseEntity(DELETE_POST_NOT_POSSIBLE);
    }

    @ExceptionHandler(MemberFilledAnonymousName.class)
    ResponseEntity<ErrorResponse> handle(MemberFilledAnonymousName ex) {
        return ErrorResponse.getErrorResponseEntity(MEMBER_FILLED_ANON_NAME);
    }

    @ExceptionHandler(UpdatePostNotPossible.class)
    ResponseEntity<ErrorResponse> handle(UpdatePostNotPossible ex) {
        return ErrorResponse.getErrorResponseEntity(UPDATE_POST_NOT_POSSIBLE);
    }

}
