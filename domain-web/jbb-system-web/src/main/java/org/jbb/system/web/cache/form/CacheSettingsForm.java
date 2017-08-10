/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.cache.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CacheSettingsForm {
    private boolean applicationCacheEnabled;

    private boolean secondLevelCacheEnabled;

    private boolean queryCacheEnabled;

    private String providerName;

    private HazelcastServerSettingsForm hazelcastServerSettings = new HazelcastServerSettingsForm();

    private HazelcastClientSettingsForm hazelcastClientSettings = new HazelcastClientSettingsForm();
}
