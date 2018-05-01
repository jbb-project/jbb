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

import com.google.common.collect.Lists;

import org.springframework.data.domain.Page;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class PageWrapper<T> {

    //TODO: move to properties?
    public static final int MAX_PAGE_ITEM_DISPLAY = 15;

    private Page<T> page;
    private List<PageItem> items;
    private int currentNumber;

    public PageWrapper(Page<T> page) {
        this.page = page;
        items = Lists.newArrayList();

        currentNumber = page.getNumber() + 1; //start from 1 to match page.page

        int start;
        int size;
        if (page.getTotalPages() <= MAX_PAGE_ITEM_DISPLAY) {
            start = 1;
            size = page.getTotalPages();
        } else {
            if (currentNumber <= MAX_PAGE_ITEM_DISPLAY - MAX_PAGE_ITEM_DISPLAY / 2) {
                start = 1;
                size = MAX_PAGE_ITEM_DISPLAY;
            } else if (currentNumber >= page.getTotalPages() - MAX_PAGE_ITEM_DISPLAY / 2) {
                start = page.getTotalPages() - MAX_PAGE_ITEM_DISPLAY + 1;
                size = MAX_PAGE_ITEM_DISPLAY;
            } else {
                start = currentNumber - MAX_PAGE_ITEM_DISPLAY / 2;
                size = MAX_PAGE_ITEM_DISPLAY;
            }
        }

        for (int i = 0; i < size; i++) {
            items.add(new PageItem(start + i, (start + i) == currentNumber));
        }
    }

    public List<PageItem> getItems() {
        return items;
    }

    public int getNumber() {
        return currentNumber;
    }

    public List<T> getContent() {
        return page.getContent();
    }

    public int getSize() {
        return page.getSize();
    }

    public int getTotalPages() {
        return page.getTotalPages();
    }

    public boolean isFirstPage() {
        return page.isFirst();
    }

    public boolean isLastPage() {
        return page.isLast();
    }

    public boolean isHasPreviousPage() {
        return page.hasPrevious();
    }

    public boolean isHasNextPage() {
        return page.hasNext();
    }

    @Getter
    @AllArgsConstructor
    public static class PageItem {

        private int number;
        private boolean current;

        public int paramNumber() {
            return number - 1;
        }
    }
}