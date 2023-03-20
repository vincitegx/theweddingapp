package com.slinkdigital.user.service;

import com.slinkdigital.user.dto.EventDto;
import com.slinkdigital.user.dto.UserRequest;
import com.slinkdigital.user.exception.UserException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminService {
    
    private final KafkaTemplate<String, EventDto> kakfaTemplate;
    
    @Value("${organization.properties.mail}")
    private String organizationEmail;

    public Map<String, String> sendMessageToAdmin(UserRequest userRequest) {
        try {
            Map<String, String> data = new HashMap<>();
            data.put("message", userRequest.getMessage());
            EventDto eventDto = EventDto.builder().from(userRequest.getEmail()).to(organizationEmail).data(data).build();
            kakfaTemplate.send("email-to-admin", eventDto);
            return data;
        } catch (RuntimeException ex) {
            throw new UserException(ex.getMessage());
        }
    }
    
}
