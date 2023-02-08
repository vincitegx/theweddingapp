package com.slinkdigital.mail.controller;

import com.slinkdigital.mail.config.KafkaProperties;
import com.slinkdigital.mail.dto.EmailDto;
import com.slinkdigital.mail.dto.ProjectStatusChangeDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("/api/v1/unsecure/mail-service")
@AllArgsConstructor
@Slf4j
public class MailController {
 
    private final KafkaTemplate<String, ProjectStatusChangeDto> kakfaProducer;

    @PostMapping("/sendMail")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendEmail(@RequestBody ProjectStatusChangeDto emailDto) {
        log.info("Sending mailing request: " + emailDto.toString());
        kakfaProducer.send("notificationTopic", emailDto);
    }
    
}
