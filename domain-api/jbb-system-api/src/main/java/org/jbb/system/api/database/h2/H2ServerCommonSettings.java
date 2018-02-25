/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.database.h2;

import org.jbb.system.api.database.DatabaseProviderSettings;

import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("PMD.AbstractClassWithoutAnyMethod")
public abstract class H2ServerCommonSettings implements DatabaseProviderSettings {

    @NotBlank
    String username;

    @NotBlank
    String usernamePassword;

    @NotBlank
    String filePassword;

    @NotNull
    H2ConnectionType connectionType;

    Optional<H2EncryptionAlgorithm> encryptionAlgorithm;

}
