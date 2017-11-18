/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class NonApiRequestMatcher implements RequestMatcher {

    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    private RegexRequestMatcher apiMatcher = new RegexRequestMatcher("/api/.*", null);

    @Override
    public boolean matches(HttpServletRequest request) {
        // No CSRF due to allowedMethod
        if (allowedMethods.matcher(request.getMethod()).matches()) {
            return false;
        }

        // No CSRF due to api call
        if (apiMatcher.matches(request)) {
            return false;
        }

        // CSRF for everything else that is not an API call or an allowedMethod
        return true;
    }
}
