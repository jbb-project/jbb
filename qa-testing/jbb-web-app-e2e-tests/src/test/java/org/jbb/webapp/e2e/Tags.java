/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp.e2e;

public final class Tags {

    public static class Release {
        public static final String VER_0_3_0 = "release:0.3.0";
        public static final String VER_0_4_0 = "release:0.4.0";
        public static final String VER_0_5_0 = "release:0.5.0";
        public static final String VER_0_6_0 = "release:0.6.0";
        public static final String VER_0_7_0 = "release:0.7.0";
        public static final String VER_0_8_0 = "release:0.8.0";
    }

    public static class Feature {
        public static final String GENERAL = "feature:general";
        public static final String REGISTRATION = "feature:registration";
        public static final String AUTHENTICATION = "feature:authentication";
        public static final String EDIT_PROFILE = "feature:edit_profile";
        public static final String BOARD_SETTINGS = "feature:board_settings";
        public static final String DATABASE_SETTINGS = "feature:database_settings";
        public static final String STACKTRACE_VISIBILITY_SETTINGS = "feature:stacktrace_visibility_settings";
        public static final String MEMBER_LOCKOUT = "feature:member_lockout";
        public static final String LOGGING_SETTINGS = "feature:logging_settings";
        public static final String FORUM_MANAGEMENT = "feature:forum_management";
    }

    public static class Type {
        public static final String SMOKE = "type:smoke";
        public static final String REGRESSION = "type:regression";
        public static final String STORY = "type:story";
    }

}
