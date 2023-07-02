package com.slinkdigital.feed.service;

import com.slinkdigital.feed.service.KafkaService;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

/**
 *
 * @author TEGA
 */
@Service
public class KafkaService {

    private final Flux<ReceiverRecord<String, String>> testTopicStream;

    KafkaService() throws IOException {

        Properties kafkaProperties = PropertiesLoaderUtils.loadAllProperties("application.properties");

        kafkaProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, "reactive-consumer");
        kafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "feed-group");
        kafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        ReceiverOptions<String, String> receiverOptions = ReceiverOptions.create(kafkaProperties);

        testTopicStream = createTopicCache(receiverOptions, "feed_topic");
    }

    public Flux<ReceiverRecord<String, String>> getTestTopicFlux() {

        return testTopicStream;
    }

    private <T, G> Flux<ReceiverRecord<T, G>> createTopicCache(ReceiverOptions<T, G> receiverOptions, String topicName) {
        ReceiverOptions<T, G> options = receiverOptions.subscription(Collections.singleton(topicName));

        return KafkaReceiver.create(options).receive().cache();
    }
}
