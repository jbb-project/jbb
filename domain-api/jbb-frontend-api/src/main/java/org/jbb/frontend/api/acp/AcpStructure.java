/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.api.acp;

import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AcpStructure implements Serializable {

    private final List<Category> categories;

    public Category findCategoryByViewName(String categoryViewName) {
        return categories.stream()
                .filter(category -> category.getViewName().equals(categoryViewName))
                .findFirst().orElse(null);
    }

    public static class Builder {

        private List<Category> categories = new ArrayList<>();

        public Builder add(Category newCategory) {
            categories.add(newCategory);
            return this;
        }

        public AcpStructure build() {
            return new AcpStructure(ImmutableList.copyOf(categories));
        }

    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Category implements Serializable {
        private final String name;
        private final String viewName;

        private final List<SubCategory> subCategories;

        public Element findElementByViewName(String elementViewName) {
            return subCategories.stream().flatMap(sub -> sub.getElements().stream())
                    .filter(element -> element.getViewName().equals(elementViewName))
                    .findFirst().orElse(null);
        }

        public static class Builder {

            private String name;
            private String viewName;

            private List<SubCategory> subCategories = new ArrayList<>();

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder viewName(String viewName) {
                this.viewName = viewName;
                return this;
            }

            public Builder add(SubCategory newSubCategory) {
                subCategories.add(newSubCategory);
                return this;
            }

            public Category build() {
                return new Category(name, viewName, ImmutableList.copyOf(subCategories));
            }

        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SubCategory implements Serializable {
        private final String name;

        private final List<Element> elements;

        public static class Builder {

            private String name;

            private List<Element> elements = new ArrayList<>();

            public Builder name(String name) {
                this.name = name;
                return this;
            }


            public Builder add(Element newElement) {
                elements.add(newElement);
                return this;
            }

            public SubCategory build() {
                return new SubCategory(name, ImmutableList.copyOf(elements));
            }

        }

    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Element implements Serializable {
        private final String name;
        private final String viewName;

        public static Element of(String name, String viewName) {
            return new Element(name, viewName);
        }
    }

}
