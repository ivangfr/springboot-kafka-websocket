package com.ivanfranchin.bitcoinapi.price.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceChanged(Long id, BigDecimal value, LocalDateTime timestamp) {
}
