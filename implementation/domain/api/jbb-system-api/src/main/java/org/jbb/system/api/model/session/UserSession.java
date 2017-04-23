package org.jbb.system.api.model.session;


import java.time.Duration;
import java.time.LocalDateTime;


public interface UserSession {

    Long sessionId();

    LocalDateTime creationTime();

    LocalDateTime lastAccessedTime();

    Duration usedTime();

    Duration inactiveTime();

    String userName();

    String displayUserName();
}
