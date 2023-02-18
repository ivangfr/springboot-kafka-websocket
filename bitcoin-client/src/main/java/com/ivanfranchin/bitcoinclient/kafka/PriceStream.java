package com.ivanfranchin.bitcoinclient.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class PriceStream {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Bean
    public Consumer<Message<PriceMessage>> prices() {
        return message -> {
            PriceMessage priceMessage = message.getPayload();
            MessageHeaders messageHeaders = message.getHeaders();
            log.info("PriceMessage with id {}, value '{}' and timestamp '{}' received from bus. topic: {}, partition: {}, offset: {}, deliveryAttempt: {}",
                    priceMessage.id(), priceMessage.value(), priceMessage.timestamp(),
                    messageHeaders.get(KafkaHeaders.RECEIVED_TOPIC, String.class),
                    messageHeaders.get(KafkaHeaders.RECEIVED_PARTITION, Integer.class),
                    messageHeaders.get(KafkaHeaders.OFFSET, Long.class),
                    messageHeaders.get(IntegrationMessageHeaderAccessor.DELIVERY_ATTEMPT, AtomicInteger.class));

            simpMessagingTemplate.convertAndSend("/topic/prices", priceMessage);
        };
    }
}
