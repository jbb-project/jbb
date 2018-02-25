/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.cache;

import com.google.common.collect.Lists;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("PMD.AbstractClassWithoutAnyMethod")
public abstract class HazelcastSettings implements CacheProviderSettings {
    @NotNull
    @NotEmpty
    private List<String> members = Lists.newArrayList();

    @NotBlank
    private String groupName;

    @NotBlank
    private String groupPassword;

}
