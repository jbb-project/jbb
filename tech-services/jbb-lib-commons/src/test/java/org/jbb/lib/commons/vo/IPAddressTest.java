/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons.vo;

import com.google.common.collect.Lists;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.EqualsVerifierApi;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.EqualsMethodTester;
import org.meanbean.test.HashCodeMethodTester;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IPAddressTest {
    @Test
    public void pojoTest() {
        BeanTester beanTester = new BeanTester();
        beanTester.setIterations(3);

        beanTester.testBean(IPAddress.class);
    }

    @Test
    public void equalsTest() {
        EqualsMethodTester tester = new EqualsMethodTester();
        tester.testEqualsMethod(IPAddress.class);
    }

    @Test
    public void hashcodeTest() {
        HashCodeMethodTester tester = new HashCodeMethodTester();
        tester.testHashCodeMethod(IPAddress.class);
    }

    @Test
    public void shouldEqualsMethodUseAllFields() {
        // given
        List<Class> classes = Lists.newArrayList(IPAddress.class);

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
        IPAddress ipAddress = IPAddress.builder().value("127.0.0.1").build();
        IPAddress ipAddressConstr = new IPAddress("127.0.0.1");

        // then
        assertThat(ipAddress.getValue()).isEqualTo("127.0.0.1");
        assertThat(ipAddressConstr.getValue()).isEqualTo("127.0.0.1");
    }

}