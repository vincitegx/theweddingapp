package com.slinkdigital.user.config;

import com.slinkdigital.user.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @author TEGA
 */
@Configuration
@EnableCaching
@EnableScheduling
@Slf4j
public class CacheConfig {
    
    public static final String USERDTO = "userdto";
    
    @Bean
    public CacheManager cacheManager(){
        return new ConcurrentMapCacheManager(USERDTO);
    }
    
    @Scheduled(fixedRate = 3599999)
    public void evictAllCaches(){
        try{
            cacheManager().getCache(USERDTO).clear();
        }catch(NullPointerException ex){
            log.error(ex.getLocalizedMessage());
        }
    }    
}
