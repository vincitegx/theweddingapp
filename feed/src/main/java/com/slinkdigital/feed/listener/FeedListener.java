package com.slinkdigital.feed.listener;

import com.slinkdigital.feed.dto.PostDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author TEGA
 */
@Component
@Slf4j
public class FeedListener {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public FeedListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Cacheable(value = "feeds", key = "#postDto.id")
    @KafkaListener(topics = "feed_topic")
    public void listen(PostDto postDto) {
        log.info("getting feeds");
        // Send post details to Angular client via WebSocket
        messagingTemplate.convertAndSend("/topic/notif", postDto);
    }
}
