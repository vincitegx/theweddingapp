package com.slinkdigital.feed.service;

import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverRecord;

/**
 *
 * @author TEGA
 */
public interface KafkaService {
    
    public Flux<ReceiverRecord<String, String>> getTestTopicFlux();
    
}
