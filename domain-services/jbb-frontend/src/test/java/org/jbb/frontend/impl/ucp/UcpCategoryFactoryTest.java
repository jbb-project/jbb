/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp;

import org.jbb.frontend.impl.ucp.model.UcpCategoryEntity;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UcpCategoryFactoryTest {
    private UcpCategoryFactory ucpCategoryFactory;

    @Before
    public void setUp() throws Exception {
        ucpCategoryFactory = new UcpCategoryFactory();
    }

    @Test
    public void shouldCreateCategoriesWithElements_whenCorrectOrdering() throws Exception {
        // when
        UcpCategoryEntity overviewCategory = ucpCategoryFactory.createWithElements(
                new UcpCategoryFactory.UcpCategoryTuple("Overview", "overview"),
                new UcpCategoryFactory.UcpElementTuple("Statistics", "statistics")
        );

        UcpCategoryEntity profileCategory = ucpCategoryFactory.createWithElements(
                new UcpCategoryFactory.UcpCategoryTuple("Profile", "profile"),
                new UcpCategoryFactory.UcpElementTuple("Edit profile", "edit"),
                new UcpCategoryFactory.UcpElementTuple("Edit account settings", "editAccount")
        );

        // then
        assertThat(overviewCategory.getOrdering()).isEqualTo(1);
        assertThat(overviewCategory.getElements()).hasSize(1);
        assertThat(overviewCategory.getElements().get(0).getOrdering()).isEqualTo(1);

        assertThat(profileCategory.getOrdering()).isEqualTo(2);
        assertThat(profileCategory.getElements()).hasSize(2);
        assertThat(profileCategory.getElements().get(0).getOrdering()).isEqualTo(1);
        assertThat(profileCategory.getElements().get(1).getOrdering()).isEqualTo(2);
    }
}