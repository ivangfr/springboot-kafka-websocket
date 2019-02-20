package com.mycompany.bitcoinclient.bus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(Sink.class)
public class PriceStream {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public PriceStream(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @StreamListener(Sink.INPUT)
    public void handlePriceDto(@Payload PriceMessage priceMessage,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                               @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                               @Header(KafkaHeaders.OFFSET) Long offset,
                               @Header(IntegrationMessageHeaderAccessor.DELIVERY_ATTEMPT) Integer deliveryAttempt) {
        log.info("PriceMessage with id {}, value '{}' and timestamp '{}' received from bus. topic: {}, partition: {}, offset: {}, deliveryAttempt: {}",
                priceMessage.getId(), priceMessage.getValue(), priceMessage.getTimestamp(), topic, partition, offset, deliveryAttempt);

        simpMessagingTemplate.convertAndSend("/topic/prices", priceMessage);
    }

}
