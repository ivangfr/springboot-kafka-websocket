package com.ivanfranchin.bitcoinapi.price.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.ivanfranchin.bitcoinapi.price.model.Price;

class PriceResponseTests {

  @Test
  void testFromMapsValueAndTimestampCorrectly() {
    LocalDateTime timestamp = LocalDateTime.of(2025, 1, 15, 10, 30, 0);
    Price price = new Price(BigDecimal.valueOf(42000.50), timestamp);

    PriceResponse response = PriceResponse.from(price);

    assertThat(response.value()).isEqualByComparingTo(BigDecimal.valueOf(42000.50));
    assertThat(response.timestamp()).isEqualTo(timestamp);
  }

  @Test
  void testFromPreservesExactBigDecimalScale() {
    LocalDateTime timestamp = LocalDateTime.of(2025, 6, 1, 0, 0, 0);
    Price price = new Price(new BigDecimal("12345.99"), timestamp);

    PriceResponse response = PriceResponse.from(price);

    assertThat(response.value()).isEqualByComparingTo(new BigDecimal("12345.99"));
  }
}
