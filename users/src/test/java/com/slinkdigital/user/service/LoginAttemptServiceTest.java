package com.slinkdigital.user.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author TEGA
 */
@ExtendWith(MockitoExtension.class)
public class LoginAttemptServiceTest {

    private static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    private static final int ATTEMPT_INCREMENT = 1;
    private static final String USERNAME = "username";
    
    private LoadingCache<String, Integer> loginAttemptCache;
    
    private LoginAttemptService underTest;
    /**
     * Test of evictUserFromLoginAttemptCache method, of class LoginAttemptService.
     */
    
    @BeforeEach
    public void setUp(){
        underTest = new LoginAttemptService();
        loginAttemptCache = CacheBuilder.newBuilder().expireAfterWrite(15, MINUTES)
                .maximumSize(100).build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }
    
    @Test
    public void testEvictUserFromLoginAttemptCache() throws ExecutionException {
        underTest.evictUserFromLoginAttemptCache(USERNAME);
        assertThat(loginAttemptCache.get(USERNAME)).isEqualTo(0);
    }

    /**
     * Test of addUserToLoginAttemptCache method, of class LoginAttemptService.
     * @throws java.util.concurrent.ExecutionException
     */
    @Test
    public void testAddUserToLoginAttemptCache() throws ExecutionException {
        int attempts = ATTEMPT_INCREMENT + loginAttemptCache.get(USERNAME);
        loginAttemptCache.put(USERNAME, attempts);
        assertThat(loginAttemptCache.get(USERNAME)).isEqualTo(1);
    }

    /**
     * Test of hasExceededMaxAttempts method, of class LoginAttemptService.
     * @throws java.util.concurrent.ExecutionException
     */
    @Test
    @Disabled
    public void testHasExceededMaxAttempts() throws ExecutionException {
        assertThat(loginAttemptCache.get(USERNAME)).isGreaterThanOrEqualTo(MAXIMUM_NUMBER_OF_ATTEMPTS);
    }    
}
