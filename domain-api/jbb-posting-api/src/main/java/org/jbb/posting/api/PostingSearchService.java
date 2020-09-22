/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.api;

import org.jbb.posting.api.base.FullPost;
import org.jbb.posting.api.search.PostSearchCriteria;
import org.springframework.data.domain.Page;

public interface PostingSearchService {

    Page<FullPost> findPostsByCriteria(PostSearchCriteria criteria);

}
