package com.ivanfranchin.bitcoinapi.price;

import com.ivanfranchin.bitcoinapi.price.event.PriceChanged;
import com.ivanfranchin.bitcoinapi.price.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PriceEventEmitterTests {

    @Mock
    private StreamBridge streamBridge;

    @InjectMocks
    private PriceEventEmitter priceEventEmitter;

    @Test
    void testSendPublishesMessageWithCorrectPayloadAndBinding() {
        LocalDateTime now = LocalDateTime.of(2025, 1, 15, 10, 30, 0);
        Price price = new Price(BigDecimal.valueOf(42000), now);
        price.setId(7L);

        priceEventEmitter.send(price);

        ArgumentCaptor<PriceChanged> captor = ArgumentCaptor.forClass(PriceChanged.class);
        then(streamBridge).should().send(eq("prices-out-0"), captor.capture());

        PriceChanged sent = captor.getValue();
        assertThat(sent.id()).isEqualTo(7L);
        assertThat(sent.value()).isEqualByComparingTo(BigDecimal.valueOf(42000));
        assertThat(sent.timestamp()).isEqualTo(now);
    }

    @Test
    void testSendUsesCorrectBindingName() {
        LocalDateTime now = LocalDateTime.of(2025, 1, 15, 10, 30, 0);
        Price price = new Price(BigDecimal.valueOf(50000), now);

        priceEventEmitter.send(price);

        then(streamBridge).should().send(eq("prices-out-0"), any(PriceChanged.class));
    }
}
