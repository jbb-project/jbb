/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.cache.data;

import org.jbb.system.api.model.cache.CacheSettings;
import org.jbb.system.web.cache.form.CacheSettingsForm;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

@AllArgsConstructor
public class FormCacheSettings implements CacheSettings {
    @Delegate
    private final CacheSettingsForm form;
}