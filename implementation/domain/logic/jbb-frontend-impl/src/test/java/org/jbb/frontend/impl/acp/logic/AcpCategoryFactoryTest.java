/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp.logic;


import org.jbb.frontend.impl.acp.model.AcpCategoryEntity;
import org.jbb.frontend.impl.acp.model.AcpSubcategoryEntity;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class AcpCategoryFactoryTest {
    private AcpCategoryFactory acpCategoryFactory;

    @Before
    public void setUp() throws Exception {
        acpCategoryFactory = new AcpCategoryFactory();
    }

    @Test
    public void shouldCreateCategoriesWithSubcategories_whenCorrectOrdering() throws Exception {
        // when
        AcpCategoryEntity generalCategory = acpCategoryFactory.createWithSubcategories(
                new AcpCategoryFactory.AcpCategoryTuple("General", "general")
        );

        AcpCategoryEntity membersCategory = acpCategoryFactory.createWithSubcategories(
                new AcpCategoryFactory.AcpCategoryTuple("Members and groups", "members"),
                mock(AcpSubcategoryEntity.class)
        );

        AcpCategoryEntity systemCategory = acpCategoryFactory.createWithSubcategories(
                new AcpCategoryFactory.AcpCategoryTuple("System", "system"),
                mock(AcpSubcategoryEntity.class),
                mock(AcpSubcategoryEntity.class),
                mock(AcpSubcategoryEntity.class),
                mock(AcpSubcategoryEntity.class)
        );

        // then
        assertThat(generalCategory.getOrdering()).isEqualTo(1);
        assertThat(generalCategory.getSubcategories()).hasSize(0);

        assertThat(membersCategory.getOrdering()).isEqualTo(2);
        assertThat(membersCategory.getSubcategories()).hasSize(1);

        assertThat(systemCategory.getOrdering()).isEqualTo(3);
        assertThat(systemCategory.getSubcategories()).hasSize(4);
    }
}