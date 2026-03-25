package com.ivanfranchin.bitcoinclient.kafka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PriceEventListenerTests {

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private PriceEventListener priceEventListener;

    @Test
    void testPricesConsumerForwardsEventToWebSocketTopic() {
        LocalDateTime now = LocalDateTime.of(2025, 1, 15, 10, 30, 0);
        PriceChanged priceChanged = new PriceChanged(1L, BigDecimal.valueOf(42000.00), now);

        Consumer<Message<PriceChanged>> consumer = priceEventListener.prices();
        Message<PriceChanged> message = MessageBuilder.withPayload(priceChanged)
                .setHeader("kafka_receivedTopic", "com.ivanfranchin.bitcoin.api.price")
                .setHeader("kafka_receivedPartitionId", 0)
                .setHeader("kafka_offset", 0L)
                .build();

        consumer.accept(message);

        then(simpMessagingTemplate)
                .should()
                .convertAndSend(eq("/topic/prices"), any(PriceChanged.class));
    }
}
