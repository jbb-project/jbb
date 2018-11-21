/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.web.HttpServletRequestHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

@Component
@RequiredArgsConstructor
public class PathResolver {

    public static final String API = "/api";
    public static final String API_SLASH = API + "/";
    public static final String API_HEALTH = API_SLASH + "v1/health";
    public static final String API_HEALTH_SLASH = API_HEALTH + "/";

    private final HttpServletRequestHolder httpServletRequestHolder;
    private final UrlPathHelper urlPathHelper;

    public String getRequestPathWithinApplication() {
        HttpServletRequest request = httpServletRequestHolder.getCurrentHttpRequest();
        return urlPathHelper.getPathWithinApplication(request);
    }

    public boolean isRequestToApi() {
        String path = getRequestPathWithinApplication();
        return path.equals(API) || path.equals(API_SLASH) || path.startsWith(API_SLASH);
    }

    public boolean isRequestToHealthCheck() {
        String path = getRequestPathWithinApplication();
        return path.equals(API_HEALTH) || path.equals(API_HEALTH_SLASH);

    }
}
