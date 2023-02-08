package com.slinkdigital.feed.controller;

import com.slinkdigital.feed.dto.PostDto;
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
@RequestMapping("api/v1/feeds")
@AllArgsConstructor
@Slf4j
public class FeedController {
    
    private final KafkaTemplate<String, PostDto> kakfaProducer;

    @PostMapping("/sendMail")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendEmail(@RequestBody PostDto postDto) {
        log.info("Sending mailing request: " + postDto.toString());
        kakfaProducer.send("notificationTopic", postDto);
    }
    
}
