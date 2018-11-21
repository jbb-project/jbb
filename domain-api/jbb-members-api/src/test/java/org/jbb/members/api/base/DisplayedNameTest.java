/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.api.base;

import com.google.common.collect.Lists;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.EqualsVerifierApi;
import nl.jqno.equalsverifier.Warning;

import org.jbb.lib.test.PojoTest;
import org.junit.Test;
import org.meanbean.test.EqualsMethodTester;
import org.meanbean.test.HashCodeMethodTester;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DisplayedNameTest extends PojoTest {

    @Override
    public Class getClassUnderTest() {
        return DisplayedName.class;
    }

    @Test
    public void equalsTest() {
        EqualsMethodTester tester = new EqualsMethodTester();
        tester.testEqualsMethod(DisplayedName.class);
    }

    @Test
    public void hashcodeTest() {
        HashCodeMethodTester tester = new HashCodeMethodTester();
        tester.testHashCodeMethod(DisplayedName.class);
    }

    @Test
    public void shouldEqualsMethodUseAllFields() {
        // given
        List<Class> classes = Lists.newArrayList(DisplayedName.class);

        // when then
        for (Class clazz : classes) {
            getConfiguredEqualsVerifier(clazz).verify();
        }
    }

    private EqualsVerifierApi getConfiguredEqualsVerifier(Class clazz) {
        return EqualsVerifier.forClass(clazz)
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS);
    }

    @Test
    public void builderTest() {
        // when
        DisplayedName displayedName = DisplayedName.builder().value("John").build();
        DisplayedName displayedNameConstr = new DisplayedName("John");

        // then
        assertThat(displayedName.getValue()).isEqualTo("John");
        assertThat(displayedNameConstr.getValue()).isEqualTo("John");
    }

}