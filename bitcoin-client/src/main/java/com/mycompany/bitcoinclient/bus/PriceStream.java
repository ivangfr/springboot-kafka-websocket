package com.mycompany.bitcoinclient.bus;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(Sink.class)
public class PriceStream {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Gson gson;

    public PriceStream(SimpMessagingTemplate simpMessagingTemplate, Gson gson) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.gson = gson;
    }

    @StreamListener(Sink.INPUT)
    public void handlePriceDto(Message<String> message,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                               @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                               @Header(KafkaHeaders.OFFSET) long offset,
                               @Header(IntegrationMessageHeaderAccessor.DELIVERY_ATTEMPT) int deliveryAttempt) {
        PriceDto priceDto = gson.fromJson(message.getPayload(), PriceDto.class);
        log.info("PriceDto with value '{}' and timestamp '{}' received from bus. topic: {}, partition: {}, offset: {}, deliveryAttempt: {}",
                priceDto.getValue(), priceDto.getTimestamp(), topic, partition, offset, deliveryAttempt);

        simpMessagingTemplate.convertAndSend("/topic/prices", priceDto);
    }

}
