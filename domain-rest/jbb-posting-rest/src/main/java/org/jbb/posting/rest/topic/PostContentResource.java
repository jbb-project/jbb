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
import static org.jbb.posting.rest.PostingRestConstants.POST_CONTENTS;
import static org.jbb.posting.rest.PostingRestConstants.TOPIC_ID;
import static org.jbb.posting.rest.PostingRestConstants.TOPIC_ID_VAR;

import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.paging.PageDto;
import org.jbb.posting.api.TopicService;
import org.jbb.posting.rest.post.PostContentDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + POST_CONTENTS)
@RequestMapping(value = API_V1 + POST_CONTENTS, produces = MediaType.APPLICATION_JSON_VALUE)
public class PostContentResource {

    private final TopicService topicService;

    @GetMapping(TOPIC_ID)
    @ErrorInfoCodes({})
    @ApiOperation("Gets posts contents for topic")
    public PageDto<PostContentDto> getPosts(@PathVariable(TOPIC_ID_VAR) Long topicId,
        @Validated @ModelAttribute TopicCriteriaDto criteria) {
        return PageDto.getDto(new PageImpl<>(Lists.newArrayList(PostContentDto.builder().build())));
    }

}
