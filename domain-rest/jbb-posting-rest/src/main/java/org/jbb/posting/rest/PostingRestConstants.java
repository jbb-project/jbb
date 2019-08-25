/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.rest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PostingRestConstants {

    public static final String CONTENT = "/content";
    public static final String POSTS_CONTENTS = "/posts-contents";

    public static final String FORUMS = "/forums";
    public static final String FORUM_ID_VAR = "forumId";
    public static final String FORUM_ID = "/{" + FORUM_ID_VAR + "}";

    public static final String POSTS = "/posts";
    public static final String POST_ID_VAR = "postId";
    public static final String POST_ID = "/{" + POST_ID_VAR + "}";

    public static final String POST_SEARCH = "/post-search";

    public static final String TOPICS = "/topics";
    public static final String TOPIC_ID_VAR = "topicId";
    public static final String TOPIC_ID = "/{" + TOPIC_ID_VAR + "}";

}
