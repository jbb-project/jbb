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
import java.time.Duration;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@SuppressWarnings("PMD.AbstractClassWithoutAnyMethod")
public abstract class HazelcastSettings implements CacheProviderSettings {
    @NotNull
    private List<String> members = Lists.newArrayList();

    @NotBlank
    private String groupName;

    @NotBlank
    private String groupPassword;

    @NotNull
    private Duration connectionTimeout;

    @NotNull
    private Duration connectionAttemptPeriod;

    @Min(1)
    private int connectionAttemptLimit;

}
