/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.service;


import org.jbb.system.api.data.StackTraceVisibilityLevel;

import java.util.Optional;

public interface StackTraceService {

    Optional<String> getStackTraceAsString(Exception ex);

    StackTraceVisibilityLevel getCurrentStackTraceVisibilityLevel();

    void setStackTraceVisibilityLevel(StackTraceVisibilityLevel newVisibilityLevel);
}