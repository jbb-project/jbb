/*
 * Copyright 2006-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.jbb.lib.commons.security.expression;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.expression.OAuth2ExpressionUtils;

public class ApiSecurityExpressionMethods {

    private final Authentication authentication;

    private final ApiSecurityExpressionRoot exp;

    public ApiSecurityExpressionMethods(Authentication authentication) {
        this.authentication = authentication;
        this.exp = new ApiSecurityExpressionRoot(authentication);
    }

    public boolean isAdministratorOrHasAnyScope(String... scopes) {
        boolean scopeCheck = isNotOAuth() || hasAnyScope(scopes);
        boolean roleCheck = exp.hasRole("ROLE_ADMINISTRATOR") || isClientOnly();
        return scopeCheck && roleCheck;
    }

    public boolean notOAuthOrHasAnyScope(String... scopes) {
        return isNotOAuth() || hasAnyScope(scopes);
    }

    public boolean notOAuthButAuthenticatedOrHasAnyScope(String... scopes) {
        return (isNotOAuth() && exp.isAuthenticated()) || hasAnyScope(scopes);
    }

    private boolean hasAnyScope(String... scopes) {
        return OAuth2ExpressionUtils.hasAnyScope(authentication, scopes);
    }

    private boolean isNotOAuth() {
        return !OAuth2ExpressionUtils.isOAuth(authentication);
    }

    private boolean isClientOnly() {
        return OAuth2ExpressionUtils.isOAuthClientAuth(authentication);
    }
}
