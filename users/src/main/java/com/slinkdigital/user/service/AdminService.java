package com.slinkdigital.user.service;

import com.slinkdigital.user.dto.EventDto;
import com.slinkdigital.user.dto.UserRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
public class AdminService {

    private final KafkaTemplate<String, EventDto> kakfaTemplate;

    @Value("${organization.properties.mail}")
    private String organizationEmail;

    public void sendMessageToAdmin(UserRequest userRequest) {
        Map<String, String> data = new HashMap<>();
        data.put("message", userRequest.getMessage());
        EventDto eventDto = EventDto.builder().from(userRequest.getEmail()).to(organizationEmail).data(data).build();
        kakfaTemplate.send("email-to-admin", eventDto);
    }
}
