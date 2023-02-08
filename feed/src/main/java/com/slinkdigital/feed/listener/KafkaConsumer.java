package com.slinkdigital.feed.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slinkdigital.feed.dto.PostDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author TEGA
 */
@Slf4j
@Component
public class KafkaConsumer {

    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    SimpMessagingTemplate template;

    @KafkaListener(topics = "notificationTopic", groupId = "notification-group-id")
    public void listenSenderEmail(ConsumerRecord<String, PostDto> record) throws Exception {

       PostDto post = record.value();
        log.info("Consumed message: " + post);
        template.convertAndSend("/topic/notif", post);

    }

    /**
     * Convert json to Object
     * @param json String json value
     * @param clazz Instances of the class
     * @param <T> Object Class
     * @return Object class
     */
    private <T> T fromJson(String json, Class<T> clazz) throws Exception {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
