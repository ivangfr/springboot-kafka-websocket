package com.ivanfranchin.bitcoinapi.runner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ivanfranchin.bitcoinapi.price.PriceService;
import com.ivanfranchin.bitcoinapi.price.model.Price;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SimulationRunner implements CommandLineRunner {

  private final PriceService priceService;

  @Override
  public void run(String... args) {
    if (priceService.getLastPrice() == null) {
      priceService.savePrice(initialPrice());
    }
  }

  private Price initialPrice() {
    return new Price(BigDecimal.valueOf(37000), LocalDateTime.now());
  }
}
