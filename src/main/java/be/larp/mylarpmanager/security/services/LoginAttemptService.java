package be.larp.mylarpmanager.security.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private static final Logger logger = LoggerFactory.getLogger(LoginAttemptService.class);

    @Value("${warden.app.tentatives.maxTentatives:10}")
    private int maxTentatives;
    private final LoadingCache<String, Integer> attemptsCache;

    @Autowired
    public LoginAttemptService(@Value("${warden.app.tentatives.gracePeriodInMinutes:60}") int gracePeriodInMinutes) {
        super();
        logger.info("Creating");
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(gracePeriodInMinutes, TimeUnit.MINUTES).build(new CacheLoader<>() {
            @Override
            public Integer load(String key) {
                return 0;
            }
        });
    }

    public void loginSucceeded(String key) {
        logger.trace("Login succeeded for " + key);
        attemptsCache.invalidate(key);
    }

    public void loginFailed(String key) {
        logger.trace("Login failed for " + key);
        int attempts;
        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        try {
            return attemptsCache.get(key) >= maxTentatives;
        } catch (ExecutionException e) {
            return false;
        }
    }
}
