package org.jbb.system.api.model.session;


import java.time.LocalDateTime;

public interface SessionSettings {

    Long sessionId();

    LocalDateTime creationTime();

    LocalDateTime lastAccessedTime();

    LocalDateTime usedTime();

    LocalDateTime inactiveTime();

    String userName();

    String displayUserName();
}
