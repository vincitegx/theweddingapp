package com.slinkdigital.user.service;

import com.slinkdigital.user.dto.UserDto;

/**
 *
 * @author TEGA
 */
public interface EmailVerificationService {
    
    String registerVerificationTokenToDb(UserDto user);   
}
