package com.ivanfranchin.bitcoinapi.price.dto;

import com.ivanfranchin.bitcoinapi.price.model.Price;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceResponse(BigDecimal value, LocalDateTime timestamp) {

    public static PriceResponse from(Price price) {
        return new PriceResponse(price.getValue(), price.getTimestamp());
    }
}
