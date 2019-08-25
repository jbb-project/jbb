/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.rest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BoardRestConstants {

    public static final String BOARD_SETTINGS = "/board-settings";

    public static final String BOARD = "/board";

    public static final String FORUM_CATEGORIES = "/forum-categories";
    public static final String FORUM_CATEGORY_ID_VAR = "forumCategoryId";
    public static final String FORUM_CATEGORY_ID = "/{" + FORUM_CATEGORY_ID_VAR + "}";

    public static final String FORUMS = "/forums";
    public static final String FORUM_ID_VAR = "forumId";
    public static final String FORUM_ID = "/{" + FORUM_ID_VAR + "}";

    public static final String POSITION = "/position";

    public static final String POSTING_DETAILS = "/posting-details";

    public static final String TARGET_FORUM_CATEGORY_PARAM = "moveForumsToTargetCategoryId";

}
