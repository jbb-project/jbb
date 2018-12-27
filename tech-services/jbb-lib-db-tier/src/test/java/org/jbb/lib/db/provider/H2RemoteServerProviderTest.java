/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db.provider;

import org.jbb.lib.db.DbProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class H2RemoteServerProviderTest {
    @Mock
    private DbProperties dbPropertiesMock;

    @InjectMocks
    private H2RemoteServerProvider h2RemoteServerProvider;

    @Test
    public void getTcpNotCypherUrl() {
        // given
        given(dbPropertiesMock.h2RemoteServerConnectionType()).willReturn("tcp");
        given(dbPropertiesMock.h2RemoteServerDbEncryptionAlgorithm()).willReturn("");
        given(dbPropertiesMock.h2RemoteServerDbUrl()).willReturn("127.0.0.1");

        // when
        String jdbcUrl = h2RemoteServerProvider.getJdbcUrl();

        // then
        assertThat(jdbcUrl).isEqualTo("jdbc:h2:tcp://127.0.0.1;");
    }

    @Test
    public void getTcpCypherUrl() {
        // given
        given(dbPropertiesMock.h2RemoteServerConnectionType()).willReturn("tcp");
        given(dbPropertiesMock.h2RemoteServerDbEncryptionAlgorithm()).willReturn("AES");
        given(dbPropertiesMock.h2RemoteServerDbUrl()).willReturn("127.0.0.1");

        // when
        String jdbcUrl = h2RemoteServerProvider.getJdbcUrl();

        // then
        assertThat(jdbcUrl).isEqualTo("jdbc:h2:tcp://127.0.0.1;cipher=aes");
    }

    @Test
    public void getSslNotCypherUrl() {
        // given
        given(dbPropertiesMock.h2RemoteServerConnectionType()).willReturn("ssl");
        given(dbPropertiesMock.h2RemoteServerDbEncryptionAlgorithm()).willReturn("");
        given(dbPropertiesMock.h2RemoteServerDbUrl()).willReturn("127.0.0.1");

        // when
        String jdbcUrl = h2RemoteServerProvider.getJdbcUrl();

        // then
        assertThat(jdbcUrl).isEqualTo("jdbc:h2:ssl://127.0.0.1;");
    }

    @Test
    public void getSslCypherUrl() {
        // given
        given(dbPropertiesMock.h2RemoteServerConnectionType()).willReturn("ssl");
        given(dbPropertiesMock.h2RemoteServerDbEncryptionAlgorithm()).willReturn("AES");
        given(dbPropertiesMock.h2RemoteServerDbUrl()).willReturn("127.0.0.1");

        // when
        String jdbcUrl = h2RemoteServerProvider.getJdbcUrl();

        // then
        assertThat(jdbcUrl).isEqualTo("jdbc:h2:ssl://127.0.0.1;cipher=aes");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException_whenInvalidConnectionType() {
        // given
        given(dbPropertiesMock.h2RemoteServerConnectionType()).willReturn("http");

        // when
        h2RemoteServerProvider.getJdbcUrl();

        // then
        // throw IllegalArgumentException
    }

    @Test
    public void getUsername() {
        // given
        given(dbPropertiesMock.h2RemoteServerUsername()).willReturn("omc");

        // when
        String username = h2RemoteServerProvider.getUsername();

        // then
        assertThat(username).isEqualTo("omc");
    }

    @Test
    public void getPasswordWhenCypherEnabled() {
        // given
        given(dbPropertiesMock.h2RemoteServerPassword()).willReturn("ala");
        given(dbPropertiesMock.h2RemoteServerFilePassword()).willReturn("kot");
        given(dbPropertiesMock.h2RemoteServerDbEncryptionAlgorithm()).willReturn("DES");

        // when
        String password = h2RemoteServerProvider.getPassword();

        // then
        assertThat(password).isEqualTo("kot ala");
    }

    @Test
    public void getPasswordWhenCypherDisabled() {
        // given
        given(dbPropertiesMock.h2RemoteServerPassword()).willReturn("ala");
        given(dbPropertiesMock.h2RemoteServerDbEncryptionAlgorithm()).willReturn("");

        // when
        String password = h2RemoteServerProvider.getPassword();

        // then
        assertThat(password).isEqualTo("ala");
    }
}