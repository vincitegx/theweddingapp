package com.slinkdigital.mail.service;

import com.slinkdigital.mail.dto.EmailContentDto;
import com.slinkdigital.mail.dto.EmailDto;
import com.slinkdigital.mail.dto.ProjectStatusChangeDto;

/**
 *
 * @author TEGA
 */
public interface TemplateService {
    EmailContentDto generateEmail(ProjectStatusChangeDto emailDto);
}
