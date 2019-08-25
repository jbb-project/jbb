/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.rest.topic;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.lib.restful.paging.PageDto;
import org.jbb.posting.api.PostingService;
import org.jbb.posting.api.TopicService;
import org.jbb.posting.api.base.Post;
import org.jbb.posting.api.base.Topic;
import org.jbb.posting.api.exception.TopicNotFoundException;
import org.jbb.posting.rest.post.CreateUpdatePostDto;
import org.jbb.posting.rest.post.PostContentDto;
import org.jbb.posting.rest.post.PostDto;
import org.jbb.posting.rest.post.PostDtoTranslator;
import org.jbb.posting.rest.post.PostModelTranslator;
import org.jbb.posting.rest.post.exception.ForumIsClosed;
import org.jbb.posting.rest.post.exception.MemberFilledAnonymousName;
import org.jbb.posting.rest.topic.exception.DeleteTopicNotPossible;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.DELETE_TOPIC_NOT_POSSIBLE;
import static org.jbb.lib.restful.domain.ErrorInfo.FORUM_IS_CLOSED;
import static org.jbb.lib.restful.domain.ErrorInfo.MEMBER_FILLED_ANON_NAME;
import static org.jbb.lib.restful.domain.ErrorInfo.TOPIC_NOT_FOUND;
import static org.jbb.posting.rest.PostingRestAuthorize.PERMIT_ALL_OR_OAUTH_POST_CREATE_SCOPE;
import static org.jbb.posting.rest.PostingRestAuthorize.PERMIT_ALL_OR_OAUTH_POST_DELETE_SCOPE;
import static org.jbb.posting.rest.PostingRestAuthorize.PERMIT_ALL_OR_OAUTH_POST_READ_SCOPE;
import static org.jbb.posting.rest.PostingRestConstants.POSTS;
import static org.jbb.posting.rest.PostingRestConstants.POSTS_CONTENTS;
import static org.jbb.posting.rest.PostingRestConstants.TOPICS;
import static org.jbb.posting.rest.PostingRestConstants.TOPIC_ID;
import static org.jbb.posting.rest.PostingRestConstants.TOPIC_ID_VAR;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + TOPICS)
@RequestMapping(value = API_V1 + TOPICS + TOPIC_ID, produces = MediaType.APPLICATION_JSON_VALUE)
public class TopicResource {

    private final PostingService postingService;
    private final TopicService topicService;

    private final PostDtoTranslator postTranslator;
    private final PostModelTranslator postModelTranslator;
    private final TopicDtoTranslator topicTranslator;
    private final TopicCriteriaTranslator topicCriteriaTranslator;

    @PostMapping(value = POSTS, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ErrorInfoCodes({TOPIC_NOT_FOUND, MEMBER_FILLED_ANON_NAME})
    @ApiOperation("Creates post in topic")
    @PreAuthorize(PERMIT_ALL_OR_OAUTH_POST_CREATE_SCOPE)
    public PostDto createPost(@PathVariable(TOPIC_ID_VAR) Long topicId,
                              @Validated @RequestBody CreateUpdatePostDto createUpdateTopic)
            throws TopicNotFoundException {
        Topic topic = topicService.getTopic(topicId);
        Post newPost = postingService
                .createPost(topic.getId(), postModelTranslator.toPostModel(createUpdateTopic, topic.getForumId()));
        return postTranslator.toDto(newPost);
    }

    @GetMapping
    @ErrorInfoCodes({TOPIC_NOT_FOUND})
    @ApiOperation("Gets topic by id")
    @PreAuthorize(PERMIT_ALL_OR_OAUTH_POST_READ_SCOPE)
    public TopicDto getTopic(@PathVariable(TOPIC_ID_VAR) Long topicId)
            throws TopicNotFoundException {
        Topic topic = topicService.getTopic(topicId);
        return topicTranslator.toDto(topic);
    }

    @GetMapping(POSTS)
    @ErrorInfoCodes({TOPIC_NOT_FOUND})
    @ApiOperation("Gets posts for topic")
    @PreAuthorize(PERMIT_ALL_OR_OAUTH_POST_READ_SCOPE)
    public PageDto<PostDto> getPosts(@PathVariable(TOPIC_ID_VAR) Long topicId,
                                     @Validated @ModelAttribute TopicCriteriaDto criteria) throws TopicNotFoundException {
        Topic topic = topicService.getTopic(topicId);
        Page<PostDto> posts = topicService
                .getPostsForTopic(topic.getId(), topicCriteriaTranslator.toTopicView(criteria))
                .map(postTranslator::toDto);
        return PageDto.getDto(posts);
    }

    @GetMapping(POSTS_CONTENTS)
    @ErrorInfoCodes({TOPIC_NOT_FOUND})
    @ApiOperation("Gets posts with contents for topic")
    @PreAuthorize(PERMIT_ALL_OR_OAUTH_POST_READ_SCOPE)
    public PageDto<PostContentDto> getContentPosts(@PathVariable(TOPIC_ID_VAR) Long topicId,
                                                   @Validated @ModelAttribute TopicCriteriaDto criteria) throws TopicNotFoundException {
        Topic topic = topicService.getTopic(topicId);
        Page<PostContentDto> posts = topicService
                .getFullPostsForTopic(topic.getId(), topicCriteriaTranslator.toTopicView(criteria))
                .map(postTranslator::toContentDto);
        return PageDto.getDto(posts);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Removes topic by id")
    @ErrorInfoCodes({TOPIC_NOT_FOUND, DELETE_TOPIC_NOT_POSSIBLE})
    @PreAuthorize(PERMIT_ALL_OR_OAUTH_POST_DELETE_SCOPE)
    public void topicDelete(@PathVariable(TOPIC_ID_VAR) Long topicId)
            throws TopicNotFoundException {
        Topic topic = topicService.getTopic(topicId);
        postModelTranslator.assertDeleteTopicPrivileges();
        topicService.removeTopic(topic.getId());
    }

    @ExceptionHandler(MemberFilledAnonymousName.class)
    ResponseEntity<ErrorResponse> handle(MemberFilledAnonymousName ex) {
        return ErrorResponse.getErrorResponseEntity(MEMBER_FILLED_ANON_NAME);
    }

    @ExceptionHandler(DeleteTopicNotPossible.class)
    ResponseEntity<ErrorResponse> handle(DeleteTopicNotPossible ex) {
        return ErrorResponse.getErrorResponseEntity(DELETE_TOPIC_NOT_POSSIBLE);
    }

    @ExceptionHandler(ForumIsClosed.class)
    ResponseEntity<ErrorResponse> handle(ForumIsClosed ex) {
        return ErrorResponse.getErrorResponseEntity(FORUM_IS_CLOSED);
    }

}
