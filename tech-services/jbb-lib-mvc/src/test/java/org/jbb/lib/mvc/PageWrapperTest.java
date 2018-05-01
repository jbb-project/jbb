/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.assertj.core.util.Lists;
import org.jbb.lib.mvc.PageWrapper.PageItem;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PageWrapperTest {

    private RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
            .filteredBy(CharacterPredicates.LETTERS).build();

    @Test
    public void pageItemTest() throws Exception {
        // given
        Page<String> page = new PageImpl<>(getContentWithSize(10));

        // when
        PageWrapper<String> pageWrapper = new PageWrapper<>(page);
        PageItem pageItem = pageWrapper.getItems().get(0);

        // then
        assertThat(pageItem.getNumber()).isEqualTo(1);
        assertThat(pageItem.isCurrent()).isTrue();
        assertThat(pageItem.paramNumber()).isEqualTo(0);
    }

    @Test
    public void singlePageTest_forBelowMaxPageItemDisplay() throws Exception {
        // given
        Page<String> page = new PageImpl<>(getContentWithSize(10));

        // when
        PageWrapper<String> pageWrapper = new PageWrapper<>(page);

        // then
        assertThat(pageWrapper.getItems()).hasSize(1);
        assertThat(pageWrapper.getNumber()).isEqualTo(1);
        assertThat(pageWrapper.getContent()).hasSize(10);
        assertThat(pageWrapper.getSize()).isEqualTo(0);
        assertThat(pageWrapper.getTotalPages()).isEqualTo(1);
        assertThat(pageWrapper.isFirstPage()).isTrue();
        assertThat(pageWrapper.isLastPage()).isTrue();
        assertThat(pageWrapper.isHasNextPage()).isFalse();
        assertThat(pageWrapper.isHasPreviousPage()).isFalse();
    }

    @Test
    public void secondPageTest_forBelowMaxPageItemDisplay() throws Exception {
        // given
        Page<String> page = new PageImpl<>(getContentWithSize(20), new PageRequest(1, 10), 20);

        // when
        PageWrapper<String> pageWrapper = new PageWrapper<>(page);

        // then
        assertThat(pageWrapper.getItems()).hasSize(2);
        assertThat(pageWrapper.getNumber()).isEqualTo(2);
        assertThat(pageWrapper.getContent()).hasSize(20);
        assertThat(pageWrapper.getSize()).isEqualTo(10);
        assertThat(pageWrapper.getTotalPages()).isEqualTo(2);
        assertThat(pageWrapper.isFirstPage()).isFalse();
        assertThat(pageWrapper.isLastPage()).isTrue();
        assertThat(pageWrapper.isHasNextPage()).isFalse();
        assertThat(pageWrapper.isHasPreviousPage()).isTrue();
    }

    @Test
    public void secondPageTest_forMoreThanMaxPageItemDisplay() throws Exception {
        // given
        Page<String> page = new PageImpl<>(getContentWithSize(200), new PageRequest(1, 10), 200);

        // when
        PageWrapper<String> pageWrapper = new PageWrapper<>(page);

        // then
        assertThat(pageWrapper.getItems()).hasSize(15);
        assertThat(pageWrapper.getNumber()).isEqualTo(2);
        assertThat(pageWrapper.getContent()).hasSize(200);
        assertThat(pageWrapper.getSize()).isEqualTo(10);
        assertThat(pageWrapper.getTotalPages()).isEqualTo(20);
        assertThat(pageWrapper.isFirstPage()).isFalse();
        assertThat(pageWrapper.isLastPage()).isFalse();
        assertThat(pageWrapper.isHasNextPage()).isTrue();
        assertThat(pageWrapper.isHasPreviousPage()).isTrue();
    }

    @Test
    public void sixthPageTest_forMoreThanMaxPageItemDisplay() throws Exception {
        // given
        Page<String> page = new PageImpl<>(getContentWithSize(200), new PageRequest(6, 10), 200);

        // when
        PageWrapper<String> pageWrapper = new PageWrapper<>(page);

        // then
        assertThat(pageWrapper.getItems()).hasSize(15);
        assertThat(pageWrapper.getNumber()).isEqualTo(7);
        assertThat(pageWrapper.getContent()).hasSize(200);
        assertThat(pageWrapper.getSize()).isEqualTo(10);
        assertThat(pageWrapper.getTotalPages()).isEqualTo(20);
        assertThat(pageWrapper.isFirstPage()).isFalse();
        assertThat(pageWrapper.isLastPage()).isFalse();
        assertThat(pageWrapper.isHasNextPage()).isTrue();
        assertThat(pageWrapper.isHasPreviousPage()).isTrue();
    }

    @Test
    public void tenthPage_forMoreThanMaxPageItemDisplay() throws Exception {
        // given
        Page<String> page = new PageImpl<>(getContentWithSize(200), new PageRequest(10, 10), 200);

        // when
        PageWrapper<String> pageWrapper = new PageWrapper<>(page);

        // then
        assertThat(pageWrapper.getItems()).hasSize(15);
        assertThat(pageWrapper.getNumber()).isEqualTo(11);
        assertThat(pageWrapper.getContent()).hasSize(200);
        assertThat(pageWrapper.getSize()).isEqualTo(10);
        assertThat(pageWrapper.getTotalPages()).isEqualTo(20);
        assertThat(pageWrapper.isFirstPage()).isFalse();
        assertThat(pageWrapper.isLastPage()).isFalse();
        assertThat(pageWrapper.isHasNextPage()).isTrue();
        assertThat(pageWrapper.isHasPreviousPage()).isTrue();
    }

    @Test
    public void seventeenthPageTest_forMoreThanMaxPageItemDisplay() throws Exception {
        // given
        Page<String> page = new PageImpl<>(getContentWithSize(200), new PageRequest(17, 10), 200);

        // when
        PageWrapper<String> pageWrapper = new PageWrapper<>(page);

        // then
        assertThat(pageWrapper.getItems()).hasSize(15);
        assertThat(pageWrapper.getNumber()).isEqualTo(18);
        assertThat(pageWrapper.getContent()).hasSize(200);
        assertThat(pageWrapper.getSize()).isEqualTo(10);
        assertThat(pageWrapper.getTotalPages()).isEqualTo(20);
        assertThat(pageWrapper.isFirstPage()).isFalse();
        assertThat(pageWrapper.isLastPage()).isFalse();
        assertThat(pageWrapper.isHasNextPage()).isTrue();
        assertThat(pageWrapper.isHasPreviousPage()).isTrue();
    }

    private List<String> getContentWithSize(int size) {
        List<String> result = Lists.newArrayList();
        for (int i = 1; i <= size; i++) {
            result.add(randomStringGenerator.generate(15));
        }
        return result;
    }

}