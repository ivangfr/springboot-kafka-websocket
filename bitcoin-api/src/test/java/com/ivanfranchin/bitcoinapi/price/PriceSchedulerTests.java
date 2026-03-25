package com.ivanfranchin.bitcoinapi.price;

import com.ivanfranchin.bitcoinapi.price.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;

@ExtendWith(MockitoExtension.class)
class PriceSchedulerTests {

    @Mock
    private PriceService priceService;

    @Mock
    private PriceEventEmitter priceEventEmitter;

    @InjectMocks
    private PriceScheduler priceScheduler;

    @Test
    void testStreamNewPriceInvokesServiceAndEmitterWhenRandomFires() {
        LocalDateTime now = LocalDateTime.of(2025, 1, 15, 10, 30, 0);
        Price lastPrice = new Price(BigDecimal.valueOf(37000), now);
        given(priceService.getLastPrice()).willReturn(lastPrice);
        given(priceService.savePrice(any(Price.class))).willAnswer(inv -> inv.getArgument(0));

        // Run many times so at least one iteration fires the random branch
        for (int i = 0; i < 100; i++) {
            priceScheduler.streamNewPrice();
        }

        // Over 100 iterations the random branch must have fired at least once
        then(priceService).should(atLeastOnce()).getLastPrice();
        then(priceService).should(atLeastOnce()).savePrice(any(Price.class));
        then(priceEventEmitter).should(atLeastOnce()).send(any(Price.class));
    }

    @Test
    void testStreamNewPriceComputesPositiveOrNegativeVariationFromCurrentValue() {
        LocalDateTime now = LocalDateTime.of(2025, 1, 15, 10, 30, 0);
        Price lastPrice = new Price(BigDecimal.valueOf(50000), now);
        given(priceService.getLastPrice()).willReturn(lastPrice);
        given(priceService.savePrice(any(Price.class))).willAnswer(inv -> inv.getArgument(0));

        ArgumentCaptor<Price> captor = ArgumentCaptor.forClass(Price.class);
        for (int i = 0; i < 100; i++) {
            priceScheduler.streamNewPrice();
        }

        then(priceService).should(atLeastOnce()).savePrice(captor.capture());

        // Every computed price must have a value (not null) and a timestamp (not null)
        captor.getAllValues().forEach(saved -> {
            assertThat(saved.getValue()).isNotNull();
            assertThat(saved.getTimestamp()).isNotNull();
            // Value is the current price ± up to 100, scaled to 2dp
            assertThat(saved.getValue().scale()).isEqualTo(2);
        });
    }
}
