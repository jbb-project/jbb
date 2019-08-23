/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp;

import org.jbb.frontend.api.acp.AcpStructure;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class AcpStructureProvider {

    private final AcpStructure acpStructure;

    public AcpStructureProvider() {
        acpStructure = new AcpStructure.Builder()
                .add(new AcpStructure.Category.Builder().name("General").viewName("general")
                        .add(new AcpStructure.SubCategory.Builder().name("Board configuration")
                                .add(AcpStructure.Element.of("Board settings", "board"))
                                .add(AcpStructure.Element.of("Member registration settings", "registration"))
                                .add(AcpStructure.Element.of("Member lockout settings", "lockout"))
                                .add(AcpStructure.Element.of("Forum management", "forums"))
                                .add(AcpStructure.Element.of("FAQ settings", "faq"))
                                .build())
                        .add(new AcpStructure.SubCategory.Builder().name("Server configuration")
                                .add(AcpStructure.Element.of("Logging & debugging settings", "logging"))
                                .add(AcpStructure.Element.of("Cache settings", "cache"))
                                .build())
                        .build())
                .add(new AcpStructure.Category.Builder().name("Members and groups").viewName("members")
                        .add(new AcpStructure.SubCategory.Builder().name("Members")
                                .add(AcpStructure.Element.of("Search & manage members", "manage"))
                                .add(AcpStructure.Element.of("Create new member", "create"))
                                .build())
                        .add(new AcpStructure.SubCategory.Builder().name("Member locks")
                                .add(AcpStructure.Element.of("Search member locks", "locks"))
                                .build())
                        .build())
                .add(new AcpStructure.Category.Builder().name("Permissions").viewName("permissions")
                        .add(new AcpStructure.SubCategory.Builder().name("Global permissions")
                                .add(AcpStructure.Element.of("Member permissions", "global-members"))
                                .add(AcpStructure.Element.of("Administrator permissions", "global-administrators"))
                                .build())
                        .add(new AcpStructure.SubCategory.Builder().name("Permission roles")
                                .add(AcpStructure.Element.of("Member permission roles", "role-members"))
                                .add(AcpStructure.Element.of("Administrator permission roles", "role-administrators"))
                                .build())
                        .add(new AcpStructure.SubCategory.Builder().name("Effective permissions")
                                .add(AcpStructure.Element.of("View member permissions", "effective-members"))
                                .add(AcpStructure.Element.of("View administrator permissions", "effective-administrators"))
                                .build())
                        .build())
                .add(new AcpStructure.Category.Builder().name("System").viewName("system")
                        .add(new AcpStructure.SubCategory.Builder().name("Sessions")
                                .add(AcpStructure.Element.of("Sessions management", "sessions"))
                                .build())
                        .add(new AcpStructure.SubCategory.Builder().name("Storage")
                                .add(AcpStructure.Element.of("Database settings", "database"))
                                .build())
                        .add(new AcpStructure.SubCategory.Builder().name("Integration")
                                .add(AcpStructure.Element.of("OAuth clients", "oauth"))
                                .build())
                        .add(new AcpStructure.SubCategory.Builder().name("Maintenance")
                                .add(AcpStructure.Element.of("Metrics settings", "metrics"))
                                .add(AcpStructure.Element.of("Monitoring", "monitoring"))
                                .build())
                        .build())
                .build();
    }

}
