/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.install;

import org.hibernate.validator.constraints.Length;
import org.jbb.install.cache.CacheInstallationData;
import org.jbb.install.database.DatabaseInstallationData;
import org.jbb.install.validation.ValidDatabaseInstallationData;

import java.util.Optional;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InstallationData {

    @NotEmpty
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[^\\s]+$", message = "{org.jbb.lib.commons.vo.Username.nowhitespace.message}")
    private String adminUsername;

    @NotEmpty
    @Size(min = 3, max = 64)
    private String adminDisplayedName;

    @Email
    @NotEmpty
    @Length(min = 3, max = 254)
    private String adminEmail;

    @NotBlank
    private String adminPassword;

    @NotBlank
    @Length(min = 1, max = 60)
    private String boardName;

    //        @Valid
    @ValidDatabaseInstallationData
    private DatabaseInstallationData databaseInstallationData;

    // not visible in UI
    @Builder.Default
    private Optional<CacheInstallationData> cacheInstallationData = Optional.empty();

}
