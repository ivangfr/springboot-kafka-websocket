package com.ivanfranchin.bitcoinapi.kafka;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceMessage(Long id, BigDecimal value, LocalDateTime timestamp) {
}
