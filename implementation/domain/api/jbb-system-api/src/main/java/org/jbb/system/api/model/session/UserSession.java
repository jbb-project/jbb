package org.jbb.system.api.model.session;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;


public interface UserSession {

    String sessionId();

    LocalTime creationTime();

    LocalTime lastAccessedTime();

    Duration usedTime();

    Duration inactiveTime();

    Duration timeToLive();

    String userName();

    String displayUserName();
}
