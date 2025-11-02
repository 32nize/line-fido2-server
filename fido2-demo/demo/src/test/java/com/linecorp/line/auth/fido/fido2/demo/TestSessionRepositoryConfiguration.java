package com.linecorp.line.auth.fido.fido2.demo;

import com.linecorp.line.auth.fido.fido2.server.model.Session;
import com.linecorp.line.auth.fido.fido2.server.repository.SessionRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@TestConfiguration
public class TestSessionRepositoryConfiguration {

    @Bean
    @Primary
    public SessionRepository inMemorySessionRepository() {
        return new InMemorySessionRepository();
    }

    private static class InMemorySessionRepository implements SessionRepository {
        private static class Entry {
            final Session session;
            final long expireAtMillis; // epoch millis

            Entry(Session s, long ttlMillis) {
                this.session = s;
                this.expireAtMillis = System.currentTimeMillis() + ttlMillis;
            }

            boolean expired() {
                return System.currentTimeMillis() > expireAtMillis;
            }
        }

        private final Map<String, Entry> store = new ConcurrentHashMap<>();

        @Override
        public Session getSession(String id) {
            Entry e = store.get(id);
            if (e == null) return null;
            if (e.expired()) {
                store.remove(id);
                return null;
            }
            return e.session;
        }

        @Override
        public void save(Session session) {
            // default ttl: 3 minutes if not provided by tests
            long ttlMillis = 180_000L;
            store.put(session.getId(), new Entry(session, ttlMillis));
        }

        @Override
        public void update(Session session) {
            Entry prev = store.get(session.getId());
            long ttlMillis = 180_000L;
            if (prev != null) {
                long remaining = prev.expireAtMillis - System.currentTimeMillis();
                if (remaining > 0) ttlMillis = remaining;
            }
            store.put(session.getId(), new Entry(session, ttlMillis));
        }
    }
}