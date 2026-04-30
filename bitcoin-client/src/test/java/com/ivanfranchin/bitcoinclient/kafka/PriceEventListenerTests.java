package com.ivanfranchin.bitcoinclient.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;

@ExtendWith(MockitoExtension.class)
class PriceEventListenerTests {

  @Mock private SimpMessagingTemplate simpMessagingTemplate;

  @InjectMocks private PriceEventListener priceEventListener;

  @Test
  void testPricesConsumerForwardsEventToWebSocketTopic() {
    LocalDateTime now = LocalDateTime.of(2025, 1, 15, 10, 30, 0);
    PriceChanged priceChanged = new PriceChanged(1L, BigDecimal.valueOf(42000.00), now);

    Consumer<Message<PriceChanged>> consumer = priceEventListener.prices();
    Message<PriceChanged> message =
        MessageBuilder.withPayload(priceChanged)
            .setHeader("kafka_receivedTopic", "com.ivanfranchin.bitcoin.api.price")
            .setHeader("kafka_receivedPartitionId", 0)
            .setHeader("kafka_offset", 0L)
            .build();

    consumer.accept(message);

    ArgumentCaptor<PriceChanged> captor = ArgumentCaptor.forClass(PriceChanged.class);
    then(simpMessagingTemplate).should().convertAndSend(eq("/topic/prices"), captor.capture());

    PriceChanged forwarded = captor.getValue();
    assertThat(forwarded.id()).isEqualTo(1L);
    assertThat(forwarded.value()).isEqualByComparingTo(BigDecimal.valueOf(42000.00));
    assertThat(forwarded.timestamp()).isEqualTo(now);
  }

  @Test
  void testPricesConsumerForwardsExactPayloadInstance() {
    LocalDateTime now = LocalDateTime.of(2025, 3, 10, 8, 0, 0);
    PriceChanged priceChanged = new PriceChanged(99L, BigDecimal.valueOf(65000.75), now);

    Consumer<Message<PriceChanged>> consumer = priceEventListener.prices();
    Message<PriceChanged> message =
        MessageBuilder.withPayload(priceChanged)
            .setHeader("kafka_receivedTopic", "prices")
            .setHeader("kafka_receivedPartitionId", 1)
            .setHeader("kafka_offset", 42L)
            .build();

    consumer.accept(message);

    ArgumentCaptor<PriceChanged> captor = ArgumentCaptor.forClass(PriceChanged.class);
    then(simpMessagingTemplate).should().convertAndSend(eq("/topic/prices"), captor.capture());

    PriceChanged forwarded = captor.getValue();
    assertThat(forwarded.id()).isEqualTo(99L);
    assertThat(forwarded.value()).isEqualByComparingTo(new BigDecimal("65000.75"));
    assertThat(forwarded.timestamp()).isEqualTo(now);
  }
}
