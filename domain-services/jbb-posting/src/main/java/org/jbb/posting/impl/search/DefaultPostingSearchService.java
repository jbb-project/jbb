/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.impl.search;

import org.apache.commons.lang3.Validate;
import org.jbb.posting.api.PostingSearchService;
import org.jbb.posting.api.base.FullPost;
import org.jbb.posting.api.search.PostSearchCriteria;
import org.jbb.posting.impl.base.PostTranslator;
import org.jbb.posting.impl.base.dao.PostDocumentRepository;
import org.jbb.posting.impl.base.dao.PostRepository;
import org.jbb.posting.impl.base.model.PostDocument;
import org.jbb.posting.impl.base.model.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultPostingSearchService implements PostingSearchService {
    private final PostTranslator postTranslator;
    private final PostRepository postRepository;

    private final PostDocumentRepository postDocumentRepository;

    @Override
    public Page<FullPost> findPostsByCriteria(PostSearchCriteria criteria) {
        Validate.notNull(criteria);
        return postDocumentRepository.findByContent(criteria.getPhrase(), criteria.getPageRequest())
                .map(this::toModel);
    }

    private FullPost toModel(PostDocument document) {
        PostEntity post = postRepository.findById(Long.valueOf(document.getId()))
                .orElseThrow(IllegalStateException::new);
        return postTranslator.toFullModel(post);
    }
}
