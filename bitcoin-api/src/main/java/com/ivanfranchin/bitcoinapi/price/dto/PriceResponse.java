package com.ivanfranchin.bitcoinapi.price.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ivanfranchin.bitcoinapi.price.model.Price;

public record PriceResponse(BigDecimal value, LocalDateTime timestamp) {

  public static PriceResponse from(Price price) {
    return new PriceResponse(price.getValue(), price.getTimestamp());
  }
}
