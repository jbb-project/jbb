/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.rest.topic;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.posting.rest.PostingRestConstants.TOPICS;
import static org.jbb.posting.rest.PostingRestConstants.TOPIC_ID;
import static org.jbb.posting.rest.PostingRestConstants.TOPIC_ID_VAR;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.posting.api.PostingService;
import org.jbb.posting.api.TopicService;
import org.jbb.posting.rest.post.CreateUpdatePostDto;
import org.jbb.posting.rest.post.PostDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + TOPICS)
@RequestMapping(value = API_V1 + TOPICS, produces = MediaType.APPLICATION_JSON_VALUE)
public class TopicResource {

    private final PostingService postingService;
    private final TopicService topicService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ErrorInfoCodes({})
    @ApiOperation("Creates post in topic")
    public PostDto createPost(@PathVariable(TOPIC_ID_VAR) Long topicId,
        @Validated @RequestBody CreateUpdatePostDto createUpdateTopic) {
        return PostDto.builder().build();
    }

    @GetMapping(TOPIC_ID)
    @ErrorInfoCodes({})
    @ApiOperation("Gets topic by id")
    public TopicDto getTopic(@PathVariable(TOPIC_ID_VAR) Long topicId) {
        return TopicDto.builder().build();
    }

    @DeleteMapping(TOPIC_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Removes topic by id")
    @ErrorInfoCodes({})
    public void topicDelete(@PathVariable(TOPIC_ID_VAR) Long topicId) {

    }


}
