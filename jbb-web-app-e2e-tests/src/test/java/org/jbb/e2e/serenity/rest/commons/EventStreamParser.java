/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.commons;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.regex.Pattern;

public class EventStreamParser {
    private static final String DATA = "data";
    private static final String ID = "id";
    private static final String EVENT = "event";
    private static final String RETRY = "retry";

    private static final String DEFAULT_EVENT = "message";
    private static final String EMPTY_STRING = "";
    private static final Pattern DIGITS_ONLY = Pattern.compile("^[\\d]+$");

    private StringBuffer data = new StringBuffer();
    private String id;
    private String eventName = DEFAULT_EVENT;
    private Long retry;
    private List<MessageEvent> events = Lists.newArrayList();

    public static EventStreamParser parser() {
        return new EventStreamParser();
    }

    public List<MessageEvent> parseEvents(String lines) {
        lines(lines);
        ImmutableList<MessageEvent> result = ImmutableList.copyOf(events);
        events = Lists.newArrayList();
        return result;
    }

    private void lines(String lines) {
        String[] lineArray = lines.split("\n", -1);
        for (String line : lineArray) {
            line(line);
        }
    }

    private void line(String line) {
        int colonIndex;
        if (line.trim().isEmpty()) {
            dispatchEvent();
        } else if (line.startsWith(":")) {
            // ignore
        } else if ((colonIndex = line.indexOf(":")) != -1) {
            String field = line.substring(0, colonIndex);
            String value = line.substring(colonIndex + 1).replaceFirst(" ", EMPTY_STRING);
            processField(field, value);
        } else {
            processField(line.trim(), EMPTY_STRING); // The spec doesn't say we need to trim the line, but I assume that's an oversight.
        }
    }

    private void processField(String field, String value) {
        if (DATA.equals(field)) {
            data.append(value).append("\n");
        } else if (ID.equals(field)) {
            id = value;
        } else if (EVENT.equals(field)) {
            eventName = value;
        } else if (RETRY.equals(field) && isNumber(value)) {
            retry = Long.parseLong(value);
        }
    }

    private boolean isNumber(String value) {
        return DIGITS_ONLY.matcher(value).matches();
    }

    private void dispatchEvent() {
        if (data.length() == 0) {
            return;
        }
        String dataString = data.toString();
        if (dataString.endsWith("\n")) {
            dataString = dataString.substring(0, dataString.length() - 1);
        }

        MessageEvent messageEvent = MessageEvent.of(id, eventName, dataString);
        events.add(messageEvent);


        data = new StringBuffer();
        eventName = DEFAULT_EVENT;
    }


}