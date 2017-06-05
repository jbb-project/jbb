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

import org.jbb.board.api.forum.Forum;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import static org.assertj.core.api.Assertions.assertThat;

public class ForumFormTest {

    @Test
    public void pojoTest() throws Exception {
        BeanTester beanTester = new BeanTester();
        beanTester.setIterations(3);

        beanTester.testBean(ForumForm.class);
    }

    @Test
    public void getForumModel() throws Exception {
        // given
        ForumForm form = new ForumForm();
        form.setId(1L);
        form.setCategoryId(2L);
        form.setName("name");
        form.setDescription("desc");
        form.setClosed(true);

        // when
        Forum forum = form.buildForum();

        // then
        assertThat(forum.getId()).isEqualTo(1L);
        assertThat(forum.getName()).isEqualTo("name");
        assertThat(forum.getDescription()).isEqualTo("desc");
        assertThat(forum.isClosed()).isTrue();
    }
}