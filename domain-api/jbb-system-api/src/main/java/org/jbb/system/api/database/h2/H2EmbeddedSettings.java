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

import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseProviderSettings;

@Getter
@Setter
@Builder
public class H2EmbeddedSettings implements DatabaseProviderSettings {

    @NotBlank
    private String databaseFileName;

    @NotBlank
    private String username;

    @NotBlank
    private String usernamePassword;

    @NotBlank
    private String filePassword;

    private Optional<H2EncryptionAlgorithm> encryptionAlgorithm;

    @Override
    public DatabaseProvider getDatabaseProvider() {
        return DatabaseProvider.H2_EMBEDDED;
    }
}
