package fr.thomascecil.heliodysse.adapter.in.security;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_DURATION_MS = 5 * 60 * 1000; // 10 minutes

    private static class Attempt {
        int count;
        long lastFailedTime;
    }

    private final Map<String, Attempt> attemptsCache = new ConcurrentHashMap<>();

    public void loginFailed(String email) {
        Attempt attempt = attemptsCache.getOrDefault(email, new Attempt());
        attempt.count++;
        attempt.lastFailedTime = System.currentTimeMillis();
        attemptsCache.put(email, attempt);
    }

    public void loginSucceeded(String email) {
        attemptsCache.remove(email);
    }

    public boolean isBlocked(String email) {
        Attempt attempt = attemptsCache.get(email);
        if (attempt == null) return false;

        if (attempt.count < MAX_ATTEMPTS) return false;

        long now = System.currentTimeMillis();
        if (now - attempt.lastFailedTime > BLOCK_DURATION_MS) {
            attemptsCache.remove(email);
            return false;
        }

        return true;
    }
}
