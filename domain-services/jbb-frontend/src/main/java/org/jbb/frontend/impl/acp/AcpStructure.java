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

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class AcpStructure {

    private List<Category> categories = new ArrayList<>();

    public AcpStructure() {
        add(Category.of("General", "general")
                .add(SubCategory.of("Board configuration")
                        .add(Element.of("Board settings", "board"))
                        .add(Element.of("Member registration settings", "registration"))
                        .add(Element.of("Member lockout settings", "lockout"))
                        .add(Element.of("Forum management", "forums"))
                        .add(Element.of("FAQ settings", "faq")))
                .add(SubCategory.of("Server configuration")
                        .add(Element.of("Logging & debugging settings", "logging"))
                        .add(Element.of("Cache settings", "cache"))));

        add(Category.of("Members and groups", "members")
                .add(SubCategory.of("Members")
                        .add(Element.of("Search & manage members", "manage"))
                        .add(Element.of("Create new member", "create")))
                .add(SubCategory.of("Member locks")
                        .add(Element.of("Search member locks", "locks"))));

        add(Category.of("Permissions", "permissions")
                .add(SubCategory.of("Global permissions")
                        .add(Element.of("Member permissions", "global-members"))
                        .add(Element.of("Administrator permissions", "global-administrators")))
                .add(SubCategory.of("Permission roles")
                        .add(Element.of("Member permission roles", "role-members"))
                        .add(Element.of("Administrator permission roles", "role-administrators")))
                .add(SubCategory.of("Effective permissions")
                        .add(Element.of("View member permissions", "effective-members"))
                        .add(Element.of("View administrator permissions", "effective-administrators"))));

        add(Category.of("System", "system")
                .add(SubCategory.of("Sessions")
                        .add(Element.of("Sessions management", "sessions")))
                .add(SubCategory.of("Storage")
                        .add(Element.of("Database settings", "database")))
                .add(SubCategory.of("Integration")
                        .add(Element.of("OAuth clients", "oauth")))
                .add(SubCategory.of("Maintenance")
                        .add(Element.of("Metrics settings", "metrics"))
                        .add(Element.of("Monitoring", "monitoring"))));
    }

    private AcpStructure add(Category newCategory) {
        categories.add(newCategory);
        return this;
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Category {
        private final String name;
        private final String viewName;

        private List<SubCategory> subCategories = new ArrayList<>();

        public static Category of(String name, String viewName) {
            return new Category(name, viewName);
        }

        public Category add(SubCategory newSubCategory) {
            subCategories.add(newSubCategory);
            return this;
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SubCategory {
        private final String name;

        private List<Element> elements = new ArrayList<>();

        public static SubCategory of(String name) {
            return new SubCategory(name);
        }

        public SubCategory add(Element newElement) {
            elements.add(newElement);
            return this;
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Element {
        private final String name;
        private final String viewName;

        public static Element of(String name, String viewName) {
            return new Element(name, viewName);
        }
    }

}
