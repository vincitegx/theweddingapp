package com.slinkdigital.feed.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slinkdigital.feed.dto.Message;
import com.slinkdigital.feed.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 *
 * @author TEGA
 */
@Component()
@Slf4j
public class ReactiveWebSocketHandler implements WebSocketHandler {

    private static final ObjectMapper json = new ObjectMapper();

    @Autowired()
    private KafkaService kafkaService;

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        return webSocketSession.send(kafkaService.getTestTopicFlux()
                .map(receiverRecord -> {
                    Message message = new Message("[Test] Add message", receiverRecord.value());
                    log.info(message.getMsg());
                    log.info(receiverRecord.value());
                    try {
                        return json.writeValueAsString(message);
                    } catch (JsonProcessingException e) {
                        return "Error while serializing to JSON";
                    }
                })
                .map(webSocketSession::textMessage))
                .and(webSocketSession.receive()
                        .map(WebSocketMessage::getPayloadAsText).log())
                .onErrorResume(error -> {
                    log.error("Error in Kafka subscription: " + error.getMessage(), error);
                    return Mono.empty();
                });
    }
}
