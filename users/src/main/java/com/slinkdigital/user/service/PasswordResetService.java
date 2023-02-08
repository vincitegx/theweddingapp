package com.slinkdigital.user.service;

import com.slinkdigital.user.dto.EmailRequest;
import com.slinkdigital.user.dto.PasswordResetRequest;
import java.util.Map;

/**
 *
 * @author TEGA
 */
public interface PasswordResetService {

    Map<String, String> generatePasswordResetToken(EmailRequest emailRequest);

    Map<String, String> resetAccountPassword(PasswordResetRequest passwordResetRequest);
}
