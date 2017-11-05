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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class CacheUtils {

    public static final String LOCALHOST = "127.0.0.1"; //NOSONAR

    public List<String> buildHazelcastMemberList(String members, boolean removeLocalhost) {
        if (StringUtils.isBlank(members)) {
            return Lists.newArrayList();
        }

        List<String> result = new ArrayList<>(Arrays.asList(members.split("\\s*,\\s*")));
        if (removeLocalhost) {
            result.remove(LOCALHOST);
        }
        result.remove(StringUtils.EMPTY);
        return result;
    }

}
