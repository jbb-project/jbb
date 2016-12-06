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

import org.jbb.frontend.impl.acp.model.AcpSubcategoryEntity;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AcpSubcategoryFactoryTest {
    private AcpSubcategoryFactory acpSubcategoryFactory;

    @Before
    public void setUp() throws Exception {
        acpSubcategoryFactory = new AcpSubcategoryFactory();
    }

    @Test
    public void shouldCreateSubategoriesWithElements_whenCorrectElementList() throws Exception {
        // when
        AcpSubcategoryEntity boardConfSubcategory = acpSubcategoryFactory.createWithElements(
                "Board configuration",
                new AcpSubcategoryFactory.AcpElementTuple("Board settings", "board"),
                new AcpSubcategoryFactory.AcpElementTuple("Member registration settings", "registration")
        );

        AcpSubcategoryEntity memberSubcategory = acpSubcategoryFactory.createWithElements(
                "Members",
                new AcpSubcategoryFactory.AcpElementTuple("Manage members", "manage"),
                new AcpSubcategoryFactory.AcpElementTuple("Create new member", "create"),
                new AcpSubcategoryFactory.AcpElementTuple("Purge members", "purge")
        );

        AcpSubcategoryEntity systemSubcategory = acpSubcategoryFactory.createWithElements(
                "Database",
                new AcpSubcategoryFactory.AcpElementTuple("Database settings", "database")
        );


        // then
        assertThat(boardConfSubcategory.getElements()).hasSize(2);
        assertThat(memberSubcategory.getElements()).hasSize(3);
        assertThat(systemSubcategory.getElements()).hasSize(1);
    }
}