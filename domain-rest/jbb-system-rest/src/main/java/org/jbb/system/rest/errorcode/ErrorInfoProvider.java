/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.errorcode;

import com.google.common.collect.Lists;

import org.jbb.lib.restful.domain.ErrorInfo;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class ErrorInfoProvider {

    public List<ErrorInfo> getAllErrorInfos() {
        List<ErrorInfo> errorInfos = Lists.newArrayList(ErrorInfo.values());
        errorInfos.sort(Comparator.comparing(Enum::ordinal));
        return errorInfos;
    }
}
