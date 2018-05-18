/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful.paging;

import io.swagger.annotations.ApiModel;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel("Page")
public class PageDto<T> {

    private List<T> content;

    private PageMetadataDto metadata;

    public static <T> PageDto<T> getDto(Page<T> page) {
        return (PageDto<T>) PageDto.builder()
            .content((List<Object>) page.getContent())
            .metadata(PageMetadataDto.builder()
                .currentPageNumber(page.getNumber())
                .currentPageSize(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .totalSize(page.getTotalElements())
                .sort(getSortOrderList(page.getSort()))
                .build())
            .build();
    }

    private static List<SortDto> getSortOrderList(Sort sort) {
        Iterable<Order> iterable = sort::iterator;
        return StreamSupport.stream(iterable.spliterator(), false)
            .map(order -> new SortDto(order.getProperty(), order.getDirection()))
            .collect(Collectors.toList());
    }
}
