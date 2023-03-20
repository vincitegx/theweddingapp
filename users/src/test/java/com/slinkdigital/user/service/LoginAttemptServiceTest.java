package com.slinkdigital.user.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import static java.util.concurrent.TimeUnit.MINUTES;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    @Mock
    private LoadingCache<String, Integer> loginAttemptCache;
    private LoginAttemptService underTest;
    /**
     * Test of evictUserFromLoginAttemptCache method, of class LoginAttemptService.
     */
    
    @BeforeEach
    public void setUp(){
        loginAttemptCache = CacheBuilder.newBuilder().expireAfterWrite(15, MINUTES)
                .maximumSize(100).build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }
    
    @Test
    @Disabled
    public void testEvictUserFromLoginAttemptCache() {
        Mockito.verify(underTest).evictUserFromLoginAttemptCache(USERNAME);
    }

    /**
     * Test of addUserToLoginAttemptCache method, of class LoginAttemptService.
     * @throws java.util.concurrent.ExecutionException
     */
    @Test
    @Disabled
    public void testAddUserToLoginAttemptCache() throws ExecutionException {
        int attempts = 0;
        attempts = ATTEMPT_INCREMENT + loginAttemptCache.get(USERNAME);
        Mockito.verify(loginAttemptCache).put(USERNAME, attempts);
    }

    /**
     * Test of hasExceededMaxAttempts method, of class LoginAttemptService.
     * @throws java.util.concurrent.ExecutionException
     */
    @Test
    @Disabled
    public void testHasExceededMaxAttempts() throws ExecutionException {
        Assertions.assertThat(loginAttemptCache.get(USERNAME)).isGreaterThanOrEqualTo(MAXIMUM_NUMBER_OF_ATTEMPTS);
    }    
}
