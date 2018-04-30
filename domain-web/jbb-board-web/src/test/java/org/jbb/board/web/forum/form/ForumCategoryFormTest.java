/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.web.forum.form;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.lib.test.PojoTest;
import org.junit.Test;

public class ForumCategoryFormTest extends PojoTest {

    @Override
    public Class getClassUnderTest() {
        return ForumCategoryForm.class;
    }

    @Test
    public void getForumCategoryModel() throws Exception {
        // given
        ForumCategoryForm form = new ForumCategoryForm();
        form.setName("name");
        form.setId(1L);

        // when
        ForumCategory forumCategory = form.getForumCategory(Lists.newArrayList());

        // then
        assertThat(forumCategory.getId()).isEqualTo(1L);
        assertThat(forumCategory.getName()).isEqualTo("name");
        assertThat(forumCategory.getForums()).isEmpty();

    }
}