/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AcpConstants {

    /**
     * General category
     */
    public static final String GENERAL_CATEGORY = "General";
    public static final String GENERAL_VIEW = "general";

    // Board configuration subcategory
    public static final String BOARD_CONFIGURATION_SUBCATEGORY = "Board configuration";

    public static final String BOARD_SETTINGS_ELEMENT = "Board settings";
    public static final String BOARD_SETTINGS_VIEW = "board";
    public static final String REGISTRATION_SETTINGS_ELEMENT = "Member registration settings";
    public static final String REGISTRATION_SETTINGS_VIEW = "registration";
    public static final String LOCKOUT_SETTINGS_ELEMENT = "Member lockout settings";
    public static final String LOCKOUT_SETTINGS_VIEW = "lockout";
    public static final String FORUM_MANAGEMENT_ELEMENT = "Forum management";
    public static final String FORUM_MANAGEMENT_VIEW = "forums";
    public static final String FAQ_SETTINGS_ELEMENT = "FAQ settings";
    public static final String FAQ_SETTINGS_VIEW = "faq";

    // Server configuration subcategory
    public static final String SERVER_CONFIGURATION_SUBCATEGORY = "Server configuration";

    public static final String LOGGING_SETTINGS_ELEMENT = "Logging & debugging settings";
    public static final String LOGGING_SETTINGS_VIEW = "logging";
    public static final String CACHE_SETTINGS_ELEMENT = "Cache settings";
    public static final String CACHE_SETTINGS_VIEW = "cache";

    /**
     * Members category
     */
    public static final String MEMBERS_CATEGORY = "Members and groups";
    public static final String MEMBERS_VIEW = "members";

    // Members subcategory
    public static final String MEMBERS_SUBCATEGORY = "Members";

    public static final String MANAGE_MEMBERS_ELEMENT = "Search & manage members";
    public static final String MANAGE_MEMBERS_VIEW = "manage";
    public static final String CREATE_MEMBERS_ELEMENT = "Create new member";
    public static final String CREATE_MEMBERS_VIEW = "create";

    /**
     * Permissions category
     */
    public static final String PERMISSIONS_CATEGORY = "Permissions";
    public static final String PERMISSIONS_VIEW = "permissions";

    // Global permissions subcategory
    public static final String GLOBAL_PERMISSIONS_SUBCATEGORY = "Global permissions";

    public static final String GLOBAL_PERMISSIONS_MEMBERS_ELEMENT = "For members";
    public static final String GLOBAL_PERMISSIONS_MEMBERS_VIEW = "global-members";
    public static final String GLOBAL_PERMISSIONS_ADMINISTRATORS_ELEMENT = "For administrators";
    public static final String GLOBAL_PERMISSIONS_ADMINISTRATORS_VIEW = "global-administrators";

    // Permission roles subcategory
    public static final String PERMISSION_ROLES_SUBCATEGORY = "Permission roles";

    public static final String PERMISSION_ROLE_MEMBERS_ELEMENT = "Member permission roles";
    public static final String PERMISSION_ROLE_MEMBERS_VIEW = "role-members";
    public static final String PERMISSION_ROLE_ADMINISTRATORS_ELEMENT = "Administrator permission roles";
    public static final String PERMISSION_ROLE_ADMINISTRATORS_VIEW = "role-administrators";

    // Effective permissions subcategory
    public static final String EFFECTIVE_PERMISSIONS_SUBCATEGORY = "Effective permissions";

    public static final String EFFECTIVE_PERMISSIONS_MEMBERS_ELEMENT = "View member permissions";
    public static final String EFFECTIVE_PERMISSIONS_MEMBERS_VIEW = "effective-members";
    public static final String EFFECTIVE_PERMISSIONS_ADMINISTRATORS_ELEMENT = "View administrator permissions";
    public static final String EFFECTIVE_PERMISSIONS_ADMINISTRATORS_VIEW = "effective-administrators";

    /**
     * System category
     */
    public static final String SYSTEM_CATEGORY = "System";
    public static final String SYSTEM_VIEW = "system";

    // Sessions subcategory
    public static final String SESSIONS_SUBCATEGORY = "Sessions";

    public static final String SESSIONS_MANAGEMENT_ELEMENT = "Sessions management";
    public static final String SESSIONS_MANAGEMENT_VIEW = "sessions";

    // Database subcategory
    public static final String DATABASE_SUBCATEGORY = "Database";

    public static final String DATABASE_SETTINGS_ELEMENT = "Database settings";
    public static final String DATABASE_SETTINGS_VIEW = "database";

    // Maintenance subcategory
    public static final String MAINTENANCE_SUBCATEGORY = "Maintenance";

    public static final String MONITORING_ELEMENT = "Monitoring";
    public static final String MONITORING_VIEW = "monitoring";

}
