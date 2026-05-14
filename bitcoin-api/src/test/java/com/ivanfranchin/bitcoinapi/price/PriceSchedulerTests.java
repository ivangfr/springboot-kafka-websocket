package com.ivanfranchin.bitcoinapi.price;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ivanfranchin.bitcoinapi.price.model.Price;

@ExtendWith(SpringExtension.class)
@Import(PriceScheduler.class)
class PriceSchedulerTests {

  @MockitoBean private PriceService priceService;

  @MockitoBean private PriceEventEmitter priceEventEmitter;

  @Autowired private PriceScheduler priceScheduler;

  @Test
  void testStreamNewPriceInvokesServiceAndEmitterWhenRandomFires() {
    LocalDateTime now = LocalDateTime.of(2025, 1, 15, 10, 30, 0);
    Price lastPrice = new Price(BigDecimal.valueOf(37000), now);
    when(priceService.getLastPrice()).thenReturn(lastPrice);
    when(priceService.savePrice(any(Price.class))).thenAnswer(inv -> inv.getArgument(0));

    // Run many times so at least one iteration fires the random branch
    for (int i = 0; i < 100; i++) {
      priceScheduler.streamNewPrice();
    }

    // Over 100 iterations the random branch must have fired at least once
    verify(priceService, atLeastOnce()).getLastPrice();
    verify(priceService, atLeastOnce()).savePrice(any(Price.class));
    verify(priceEventEmitter, atLeastOnce()).send(any(Price.class));
  }

  @Test
  void testStreamNewPriceComputesPositiveOrNegativeVariationFromCurrentValue() {
    LocalDateTime now = LocalDateTime.of(2025, 1, 15, 10, 30, 0);
    Price lastPrice = new Price(BigDecimal.valueOf(50000), now);
    when(priceService.getLastPrice()).thenReturn(lastPrice);
    when(priceService.savePrice(any(Price.class))).thenAnswer(inv -> inv.getArgument(0));

    ArgumentCaptor<Price> captor = ArgumentCaptor.forClass(Price.class);
    for (int i = 0; i < 100; i++) {
      priceScheduler.streamNewPrice();
    }

    verify(priceService, atLeastOnce()).savePrice(captor.capture());

    // Every computed price must have a value (not null) and a timestamp (not null)
    captor
        .getAllValues()
        .forEach(
            saved -> {
              assertThat(saved.getValue()).isNotNull();
              assertThat(saved.getTimestamp()).isNotNull();
              // Value is the current price ± up to 100, scaled to 2dp
              assertThat(saved.getValue().scale()).isEqualTo(2);
            });
  }
}
