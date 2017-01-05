/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties.encrypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.ZeroSaltGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PropertiesEncryptionTest {
    @Mock
    private PswdValueResolver pswdValueResolverMock;

    private StandardPBEStringEncryptor encryptor;

    private PropertiesEncryption propertiesEncryption;

    @Before
    public void setUp() throws Exception {
        encryptor = new StandardPBEStringEncryptor();
    }

    @Test
    public void shouldEnableEncryption_whenPasswordIsPresent() throws Exception {
        // given
        given(pswdValueResolverMock.getPassword()).willReturn(Optional.of("pass"));

        // when
        preparePropertiesEncryptionObject();

        // then
        assertThat(propertiesEncryption.isEncryptionEnabled()).isTrue();
    }

    @Test
    public void shouldDisableEncryption_whenPasswordIsAbsent() throws Exception {
        // given
        given(pswdValueResolverMock.getPassword()).willReturn(Optional.empty());

        // when
        preparePropertiesEncryptionObject();

        // then
        assertThat(propertiesEncryption.isEncryptionEnabled()).isFalse();
    }

    @Test
    public void shouldNotChangeValue_whenEncryptInvoked_andPasswordPresent_andValueIsNotSurroundedWithEncPlaceholder() throws Exception {
        // given
        given(pswdValueResolverMock.getPassword()).willReturn(Optional.of("pass"));

        // when
        preparePropertiesEncryptionObject();
        String result = propertiesEncryption.encryptIfNeeded("foo");

        // then
        assertThat(result).isEqualTo("foo");
    }

    @Test
    public void shouldNotChangeValue_whenEncryptInvoked_andPasswordAbsent_andValueIsNotSurroundedWithEncPlaceholder() throws Exception {
        // given
        given(pswdValueResolverMock.getPassword()).willReturn(Optional.empty());

        // when
        preparePropertiesEncryptionObject();
        String result = propertiesEncryption.encryptIfNeeded("foo");

        // then
        assertThat(result).isEqualTo("foo");
    }

    @Test
    public void shouldNotChangeValue_whenEncryptInvoked_andPasswordAbsent_andValueIsSurroundedWithEncPlaceholder() throws Exception {
        // given
        given(pswdValueResolverMock.getPassword()).willReturn(Optional.empty());

        // when
        preparePropertiesEncryptionObject();
        String result = propertiesEncryption.encryptIfNeeded("ENC(bar)");

        // then
        assertThat(result).isEqualTo("ENC(bar)");
    }

    @Test
    public void shouldChangeValue_whenEncryptInvoked_andPasswordPresent_andValueIsSurroundedWithEncPlaceholder() throws Exception {
        // given
        given(pswdValueResolverMock.getPassword()).willReturn(Optional.of("foo"));

        // when
        preparePropertiesEncryptionObject();
        String result = propertiesEncryption.encryptIfNeeded("ENC(bar)");

        // then
        assertThat(result).startsWith("DEC(").endsWith(")");
    }

    @Test
    public void shouldNotChangeValue_whenDecryptInvoked_andPasswordAbsent_andValueIsNotSurroundedWithDecPlaceholder() throws Exception {
        // given
        given(pswdValueResolverMock.getPassword()).willReturn(Optional.empty());

        // when
        preparePropertiesEncryptionObject();
        String result = propertiesEncryption.decryptIfNeeded("foo");

        // then
        assertThat(result).isEqualTo("foo");
    }

    @Test
    public void shouldNotChangeValue_whenDecryptInvoked_andPasswordPresent_andValueIsNotSurroundedWithDecPlaceholder() throws Exception {
        // given
        given(pswdValueResolverMock.getPassword()).willReturn(Optional.of("pass"));

        // when
        preparePropertiesEncryptionObject();
        String result = propertiesEncryption.decryptIfNeeded("foo");

        // then
        assertThat(result).isEqualTo("foo");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowISE_whenDecryptInvoked_andPasswordAbsent_andValueIsSurroundedWithDecPlaceholder() throws Exception {
        // given
        given(pswdValueResolverMock.getPassword()).willReturn(Optional.empty());

        // when
        preparePropertiesEncryptionObject();
        String result = propertiesEncryption.decryptIfNeeded("DEC(tvRZFXGAUFEqIOwRAVJUqQ==)");

        // then
        // throw IllegalStateException
    }

    @Test
    public void shouldChangeValue_whenDecryptInvoked_andPasswordPresent_andValueIsSurroundedWithDecPlaceholder() throws Exception {
        // given
        given(pswdValueResolverMock.getPassword()).willReturn(Optional.of("pass"));
        // change salt generator for test
        encryptor.setSaltGenerator(new ZeroSaltGenerator());

        // when
        preparePropertiesEncryptionObject();
        String result = propertiesEncryption.decryptIfNeeded("DEC(qG6j61WemMo=)");

        // then
        assertThat(result).isEqualTo("secret");
    }

    private void preparePropertiesEncryptionObject() {
        propertiesEncryption = new PropertiesEncryption(encryptor, pswdValueResolverMock);
    }
}