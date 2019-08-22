/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.flow;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class RedirectManager {
    private static final String REFERER_HTTP_HEADER_KEY = "referer";
    private static final String HOME_URL = "/";
    private static final String REDIRECT_PREFIX_MVC = "redirect:";

    public String goToPreviousPage(HttpServletRequest request) {
        return goToPreviousPageOr(request, HOME_URL);
    }

    public String goToPreviousPageOr(HttpServletRequest request, String noRefererUrl) {
        String referer = request.getHeader(REFERER_HTTP_HEADER_KEY);
        if (StringUtils.isNotEmpty(referer)) {
            return REDIRECT_PREFIX_MVC + referer;
        } else {
            return REDIRECT_PREFIX_MVC + noRefererUrl;
        }
    }

    public String goToPreviousPageSafe(HttpServletRequest request) {
        String referer = request.getHeader(REFERER_HTTP_HEADER_KEY);
        HttpSession session = request.getSession();
        Object pageBeforeSignIn = session.getAttribute("pageBeforeSignIn");
        if (StringUtils.isNotEmpty(referer) && pageBeforeSignIn != null) {
//            return REDIRECT_PREFIX_MVC + "/oauth-redirect";
            if (referer.contains("/signin")) {
                return REDIRECT_PREFIX_MVC + pageBeforeSignIn;
            } else {
                return REDIRECT_PREFIX_MVC + referer;
            }
        } else {
            return REDIRECT_PREFIX_MVC + HOME_URL;
        }
    }


}
