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

import com.google.common.collect.Lists;
import org.jbb.lib.test.PojoTest;
import org.junit.Test;

public class ForumCategoryEntityTest extends PojoTest {

    @Override
    public Class getClassUnderTest() {
        return ForumCategoryEntity.class;
    }

    @Test
    public void builderTest() throws Exception {
        // when
        ForumCategoryEntity forumCategoryEntity = ForumCategoryEntity.builder()
                .name("category")
                .position(1)
                .forumEntities(Lists.newArrayList())
                .build();

        // then
        assertThat(forumCategoryEntity).isNotNull();
    }
}