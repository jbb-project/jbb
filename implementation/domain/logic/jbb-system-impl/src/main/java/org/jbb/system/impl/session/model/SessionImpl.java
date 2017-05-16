package org.jbb.system.impl.session.model;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.jbb.system.api.model.session.UserSession;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class SessionImpl implements UserSession {

    @NotNull
    @NotEmpty
    @NotBlank
    private String id;

    @NotNull
    private LocalTime creationTime;

    @NotNull
    private LocalTime lastAccessedTime;

    @NotNull
    private Duration usedTime;

    @NotNull
    private Duration inactiveTime;

    @NotEmpty
    @NotNull
    @NotBlank
    private String username;

    @NotEmpty
    @NotNull
    @NotBlank
    private String displayName;

    @NotNull
    private Duration timeToLive;

    public SessionImpl(){
        this.id = "id";
        this.creationTime = LocalTime.now();
        this.lastAccessedTime = LocalTime.now();
        this.usedTime = Duration.of(0, ChronoUnit.MINUTES);
        this.inactiveTime = Duration.of(0, ChronoUnit.MINUTES);
        this.username = "username";
        this.displayName = "displayName";
        this.timeToLive = Duration.of(0, ChronoUnit.MINUTES);
    }

    @Override
    public String sessionId() {
        return id;
    }

    @Override
    public LocalTime creationTime() {
        return creationTime;
    }

    @Override
    public LocalTime lastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public Duration usedTime() {
        return usedTime;
    }

    @Override
    public Duration inactiveTime() {
        return inactiveTime;
    }

    @Override
    public Duration timeToLive() {
        return timeToLive;
    }

    @Override
    public String userName() {
        return username;
    }

    @Override
    public String displayUserName() {
        return displayName;
    }


}
