/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.logging.health;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

import ch.qos.logback.core.status.Status;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LogbackStateStorage {

    public static final int TAIL_SIZE = 3;
    public static final int STORAGE_LIMIT = 100;

    private static final NavigableMap<Long, Status> STATUSES = Maps
        .synchronizedNavigableMap(Maps.newTreeMap());

    public static void putStatus(Status status) {
        STATUSES.put(Instant.now().toEpochMilli(), status);
        cleanUpOldStatuses();
    }

    private static void cleanUpOldStatuses() {
        ArrayList<Long> sortedStatusesTimes = Lists.newArrayList(STATUSES.descendingMap().keySet());
        Long limitTime = sortedStatusesTimes.get(Math.min(sortedStatusesTimes.size(), STORAGE_LIMIT) - 1);
        STATUSES.headMap(limitTime).clear();
    }

    public static List<Status> getLastStatuses() {
        return Lists.newLinkedList(Iterables.limit(STATUSES.descendingMap().values(), TAIL_SIZE));
    }

}
