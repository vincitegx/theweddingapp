package com.slinkdigital.feed.listener;

import com.slinkdigital.feed.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author TEGA
 */
@Component
public class FeedListener {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public FeedListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "feed_topic")
    public void listen(PostDto postDto) {
        // Send post details to Angular client via WebSocket
        messagingTemplate.convertAndSend("/topic/feed", postDto);
    }
}
