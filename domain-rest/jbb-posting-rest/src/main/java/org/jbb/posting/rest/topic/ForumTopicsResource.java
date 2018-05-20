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
import static org.jbb.lib.restful.domain.ErrorInfo.FORUM_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.MEMBER_POST_WITH_ANON_NAME;
import static org.jbb.posting.rest.PostingRestConstants.FORUMS;
import static org.jbb.posting.rest.PostingRestConstants.FORUM_ID;
import static org.jbb.posting.rest.PostingRestConstants.FORUM_ID_VAR;
import static org.jbb.posting.rest.PostingRestConstants.TOPICS;

import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumService;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.paging.PageDto;
import org.jbb.posting.api.TopicService;
import org.jbb.posting.rest.post.CreateUpdatePostDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + FORUMS + FORUM_ID + TOPICS)
@RequestMapping(value = API_V1 + FORUMS + FORUM_ID
    + TOPICS, produces = MediaType.APPLICATION_JSON_VALUE)
public class ForumTopicsResource {

    private final TopicService topicService;
    private final ForumService forumService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ErrorInfoCodes({FORUM_NOT_FOUND, MEMBER_POST_WITH_ANON_NAME})
    @ApiOperation("Creates topic in forum")
    public TopicDto createTopic(@PathVariable(FORUM_ID_VAR) Long forumId,
        @Validated @RequestBody CreateUpdatePostDto createUpdateTopic) {
        Forum forum = forumService.getForum(forumId);
        return TopicDto.builder().build();
    }

    @GetMapping
    @ErrorInfoCodes({FORUM_NOT_FOUND})
    @ApiOperation("Gets topics in forum")
    public PageDto<TopicDto> getTopics(@PathVariable(FORUM_ID_VAR) Long forumId,
        @Validated @ModelAttribute TopicCriteriaDto criteria) {
        Forum forum = forumService.getForum(forumId);
        return PageDto.getDto(new PageImpl<>(Lists.newArrayList(TopicDto.builder().build())));
    }

}
