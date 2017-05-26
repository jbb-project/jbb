package org.jbb.lib.mvc.repository;


import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * This is own implementation of MapSessionRepository. The only change with compare to original implementation is method @getSessionMap
 * Method is using in administrator panel in session bookmark
 *
 */
@Repository
public class JbbSessionRepository implements SessionRepository<ExpiringSession> {

    private Integer defaultMaxInactiveInterval;

    private final Map<String, ExpiringSession> sessions;

    public JbbSessionRepository() {
        this(new ConcurrentHashMap<String, ExpiringSession>());
    }

    public JbbSessionRepository(Map<String, ExpiringSession> sessions) {
        if (sessions == null) {
            throw new IllegalArgumentException("sessions cannot be null");
        }
        this.sessions = sessions;
    }

    public void setDefaultMaxInactiveInterval(int defaultMaxInactiveInterval) {
        this.defaultMaxInactiveInterval = Integer.valueOf(defaultMaxInactiveInterval);
    }

    public Integer getDefaultMaxInactiveInterval(){
        return defaultMaxInactiveInterval;
    }

    public void save(ExpiringSession session) {
        this.sessions.put(session.getId(), new MapSession(session));
    }

    public ExpiringSession getSession(String id) {
        ExpiringSession saved = this.sessions.get(id);
        if (saved == null) {
            return null;
        }
        if (saved.isExpired()) {
            delete(saved.getId());
            return null;
        }
        return new MapSession(saved);
    }

    public Map<String, ExpiringSession> getSessionMap(){
        return sessions;
    }

    public void delete(String id) {
        this.sessions.remove(id);
    }

    public ExpiringSession createSession() {
        ExpiringSession result = new MapSession();
        if (this.defaultMaxInactiveInterval != null) {
            result.setMaxInactiveIntervalInSeconds(this.defaultMaxInactiveInterval);
        }
        return result;
    }
}
