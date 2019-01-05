/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful.sse;

import com.google.common.eventbus.Subscribe;

import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.eventbus.JbbEvent;
import org.jbb.lib.eventbus.JbbEventBusListener;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseStreamResource implements JbbEventBusListener {

    protected final UserDetailsSource userDetailsSource;
    private final ConcurrentHashMap<Long, CopyOnWriteArrayList<SseEmitter>> emittersMap = new ConcurrentHashMap<>();

    public abstract Set<Long> affectedMembers(JbbEvent jbbEvent);

    @Subscribe
    public void onJbbEvent(JbbEvent jbbEvent) {
        affectedMembers(jbbEvent).forEach(memberId -> sendToEmitterForMember(jbbEvent, memberId));
    }

    private void sendToEmitterForMember(JbbEvent jbbEvent, Long memberId) {
        CopyOnWriteArrayList<SseEmitter> emitters = getMemberEmitters(memberId);
        if (emitters == null) {
            return;
        }
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                SseEmitter.SseEventBuilder builder = SseEmitter.event()
                        .name(jbbEvent.getClass().getSimpleName().replace("Event", ""))
                        .data(jbbEvent, MediaType.APPLICATION_JSON)
                        .id(jbbEvent.getEventId());
                emitter.send(builder);
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }

    public SseEmitter getEventStream() {
        SecurityContentUser currentMember = userDetailsSource.getFromApplicationContext();
        SseEmitter emitter = new SseEmitter();
        CopyOnWriteArrayList<SseEmitter> emitters = getMemberEmitters(currentMember.getUserId());
        emitters.add(emitter);
        emitter.onTimeout(() -> emitters.remove(emitter));
        return emitter;
    }

    private CopyOnWriteArrayList<SseEmitter> getMemberEmitters(Long memberId) {
        emittersMap.putIfAbsent(memberId, new CopyOnWriteArrayList<>());
        return emittersMap.getOrDefault(memberId, new CopyOnWriteArrayList<>());
    }

}
