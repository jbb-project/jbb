/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.impl.forum.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.lib.test.PojoTest;
import org.junit.Test;

public class ForumEntityTest extends PojoTest {

    @Override
    public Class getClassUnderTest() {
        return ForumEntity.class;
    }

    @Test
    public void builderTest() throws Exception {
        // when
        ForumEntity forumEntity = ForumEntity.builder()
                .name("forum name")
                .category(ForumCategoryEntity.builder().build())
                .position(1)
                .closed(true)
                .build();

        // then
        assertThat(forumEntity).isNotNull();
    }

}