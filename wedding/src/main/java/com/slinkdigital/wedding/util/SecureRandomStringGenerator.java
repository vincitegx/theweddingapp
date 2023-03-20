package com.slinkdigital.wedding.util;

import java.security.SecureRandom;
import java.util.function.Function;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author TEGA
 */
@Component
public class SecureRandomStringGenerator implements Function<Integer, String>{

    @Override
    public String apply(Integer t) {
        return RandomStringUtils.random(t, 0, 0, true, true, null, new SecureRandom());
    }
}
