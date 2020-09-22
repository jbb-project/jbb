/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.rest.search;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.paging.PageDto;
import org.jbb.posting.api.PostingSearchService;
import org.jbb.posting.api.search.PostSearchCriteria;
import org.jbb.posting.rest.post.PostContentDto;
import org.jbb.posting.rest.post.PostDtoTranslator;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.posting.rest.PostingRestAuthorize.PERMIT_ALL_OR_OAUTH_POST_SEARCH_SCOPE;
import static org.jbb.posting.rest.PostingRestConstants.POST_SEARCH;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + POST_SEARCH)
@RequestMapping(value = API_V1 + POST_SEARCH, produces = MediaType.APPLICATION_JSON_VALUE)
public class PostSearchResource {

    private final PostingSearchService postingSearchService;

    private final PostCriteriaTranslator criteriaTranslator;
    private final PostDtoTranslator dtoTranslator;

    @GetMapping
    @ErrorInfoCodes({})
    @ApiOperation("Gets posts with query")
    @PreAuthorize(PERMIT_ALL_OR_OAUTH_POST_SEARCH_SCOPE)
    public PageDto<PostContentDto> postsGet(@Validated @ModelAttribute PostCriteriaDto postCriteria) {
        PostSearchCriteria criteria = criteriaTranslator.toModel(postCriteria);
        return PageDto.getDto(postingSearchService.findPostsByCriteria(criteria)
                .map(dtoTranslator::toContentDto));
    }
}
