package com.ivanfranchin.bitcoinapi.price.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceResponse(BigDecimal value, LocalDateTime timestamp) {
}
