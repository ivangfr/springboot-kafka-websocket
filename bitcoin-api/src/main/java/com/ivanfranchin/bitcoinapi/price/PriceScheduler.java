package com.ivanfranchin.bitcoinapi.price;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ivanfranchin.bitcoinapi.price.model.Price;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PriceScheduler {

  private final PriceService priceService;
  private final PriceEventEmitter priceEventEmitter;

  @Scheduled(cron = "${price.scheduler.cron:*/2 * * * * *}") // every 2 seconds
  public void streamNewPrice() {
    if (ThreadLocalRandom.current().nextBoolean()) {
      Price price = priceService.getLastPrice();
      price = computeNewPrice(price.getValue());
      priceService.savePrice(price);
      priceEventEmitter.send(price);
    }
  }

  private Price computeNewPrice(BigDecimal currentPrice) {
    boolean sign = ThreadLocalRandom.current().nextBoolean();
    double var = ThreadLocalRandom.current().nextDouble() * 100;
    BigDecimal variation = BigDecimal.valueOf(sign ? var : -1 * var);
    BigDecimal newValue = currentPrice.add(variation).setScale(2, RoundingMode.HALF_UP);
    return new Price(newValue, LocalDateTime.now());
  }
}
