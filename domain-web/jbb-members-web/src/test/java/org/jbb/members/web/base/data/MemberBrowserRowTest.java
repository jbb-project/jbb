/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.data;

import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.util.List;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.jbb.lib.test.PojoTest;
import org.junit.Test;
import org.meanbean.test.EqualsMethodTester;
import org.meanbean.test.HashCodeMethodTester;


public class MemberBrowserRowTest extends PojoTest {
    private LocalDateTime now = LocalDateTime.now();

    @Override
    public Class getClassUnderTest() {
        return MemberBrowserRow.class;
    }

    @Test
    public void equalsTest() throws Exception {
        EqualsMethodTester tester = new EqualsMethodTester();
        tester.getFactoryCollection().addFactory(LocalDateTime.class, () -> now);
        tester.testEqualsMethod(MemberBrowserRow.class);
    }

    @Test
    public void hashcodeTest() throws Exception {
        HashCodeMethodTester tester = new HashCodeMethodTester();
        tester.getFactoryCollection().addFactory(LocalDateTime.class, () -> now);
        tester.testHashCodeMethod(MemberBrowserRow.class);
    }

    @Test
    public void shouldEqualsMethodUseAllFields() throws Exception {
        // given
        List<Class> classes = Lists.newArrayList(MemberBrowserRow.class);

        // when then
        for (Class clazz : classes) {
            getConfiguredEqualsVerifier(clazz).verify();
        }
    }

    private EqualsVerifier getConfiguredEqualsVerifier(Class clazz) {
        return EqualsVerifier.forClass(clazz)
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS);
    }

}