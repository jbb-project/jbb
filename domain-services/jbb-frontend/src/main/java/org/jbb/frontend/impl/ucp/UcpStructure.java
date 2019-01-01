/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UcpStructure {

    private List<Category> categories = new ArrayList<>();

    public UcpStructure() {
        add(Category.of("Overview", "overview")
                .add(Element.of("Statistics", "statistics")));

        add(Category.of("Profile", "profile")
                .add(Element.of("Edit profile", "edit"))
                .add(Element.of("Edit account settings", "editAccount")));
    }

    private UcpStructure add(Category newCategory) {
        categories.add(newCategory);
        return this;
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Category {
        private final String name;
        private final String viewName;

        private List<Element> elements = new ArrayList<>();

        public static Category of(String name, String viewName) {
            return new Category(name, viewName);
        }

        public Category add(Element newElement) {
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
