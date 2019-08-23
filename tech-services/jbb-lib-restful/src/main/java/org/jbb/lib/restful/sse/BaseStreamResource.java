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

import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;

import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.eventbus.JbbEvent;
import org.jbb.lib.eventbus.JbbEventBusListener;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseStreamResource implements JbbEventBusListener {

    private final ConcurrentHashMap<Long, CopyOnWriteArrayList<SseEmitter>> emittersMap = new ConcurrentHashMap<>();

    private final UserDetailsSource userDetailsSource;

    protected abstract AffectedMembers affectedMembers(JbbEvent jbbEvent);

    @Subscribe
    public void onJbbEvent(JbbEvent jbbEvent) {
        AffectedMembers affectedMembers = affectedMembers(jbbEvent);
        if (affectedMembers.isAllAffected()) {
            emittersMap.keySet().forEach(memberId -> sendToEmitterForMember(jbbEvent, memberId));
        } else {
            affectedMembers.getMemberIds().forEach(memberId -> sendToEmitterForMember(jbbEvent, memberId));
        }
    }

    private void sendToEmitterForMember(JbbEvent jbbEvent, Long memberId) {
        CopyOnWriteArrayList<SseEmitter> emitters = getMemberEmitters(memberId);
        if (emitters == null) {
            return;
        }
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                String eventName = jbbEvent.getClass().getSimpleName().replace("Event", "");
                SseEmitter.SseEventBuilder builder = SseEmitter.event()
                        .name(eventName)
                        .data(SseEventDto.builder()
                                        .eventId(jbbEvent.getEventId())
                                        .eventName(eventName)
                                        .eventDateTime(jbbEvent.getPublishDateTime())
                                        .build()
                                , MediaType.APPLICATION_JSON)
                        .id(jbbEvent.getEventId());
                emitter.send(builder);
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }

    public SseEmitter getEventStream(Long timeout) {
        SecurityContentUser currentMember = userDetailsSource.getFromApplicationContext();
        SseEmitter emitter = new SseEmitter(timeout);
        CopyOnWriteArrayList<SseEmitter> emitters = getMemberEmitters(
                Optional.ofNullable(currentMember).map(SecurityContentUser::getUserId).orElse(0L));
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(emitter);
        });
        return emitter;
    }

    private CopyOnWriteArrayList<SseEmitter> getMemberEmitters(Long memberId) {
        emittersMap.putIfAbsent(memberId, new CopyOnWriteArrayList<>());
        return emittersMap.getOrDefault(memberId, new CopyOnWriteArrayList<>());
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class AffectedMembers {

        private boolean allAffected;

        private Set<Long> memberIds;

        public static AffectedMembers allAuthorizedToEndpointMembers() {
            return new AffectedMembers(true, Sets.newHashSet());
        }

        public static AffectedMembers onlyMembers(Set<Long> memberIds) {
            return new AffectedMembers(false, memberIds);
        }

        public static AffectedMembers noneMember() {
            return new AffectedMembers(false, Sets.newHashSet());
        }
    }

}
