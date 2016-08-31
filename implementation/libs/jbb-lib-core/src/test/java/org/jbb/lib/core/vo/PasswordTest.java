/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.core.vo;

import com.google.common.collect.Lists;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Ignore;
import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.EqualsMethodTester;
import org.meanbean.test.HashCodeMethodTester;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class PasswordTest {
    @Test
    public void pojoTest() throws Exception {
        BeanTester beanTester = new BeanTester();
        beanTester.setIterations(3);

        beanTester.testBean(Password.class);
    }

    @Test
    public void equalsTest() throws Exception {
        EqualsMethodTester tester = new EqualsMethodTester();
        tester.testEqualsMethod(Password.class);
    }

    @Test
    public void hashcodeTest() throws Exception {
        HashCodeMethodTester tester = new HashCodeMethodTester();
        tester.testHashCodeMethod(Password.class);
    }

    @Test
    public void shouldEqualsMethodUseAllFields() throws Exception {
        // given
        List<Class> classes = Lists.newArrayList(Password.class);

        // when then
        for (Class clazz : classes) {
            getConfiguredEqualsVerifier(clazz).verify();
        }
    }

    private EqualsVerifier getConfiguredEqualsVerifier(Class clazz) {
        return EqualsVerifier.forClass(clazz)
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS);
    }

    @Test
    public void builderTest() throws Exception {
        // when
        Password password = Password.builder().value("pass".toCharArray()).build();
        Password passwordConstr = new Password("pass".toCharArray());


        // then
        assertThat(password.getValue()).isEqualTo("pass".toCharArray());
        assertThat(passwordConstr.getValue()).isEqualTo("pass".toCharArray());
    }

}