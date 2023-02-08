package com.slinkdigital.wedding.util;

import java.security.SecureRandom;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author TEGA
 */
@Component
public class SecureRandomStringGenerator {

    public String generateSecureRandomString(int length){
        return RandomStringUtils.random(length, 0, 0, true, true, null, new SecureRandom());
    }
}
