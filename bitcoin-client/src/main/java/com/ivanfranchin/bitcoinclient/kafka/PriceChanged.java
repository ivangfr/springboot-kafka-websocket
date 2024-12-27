package com.ivanfranchin.bitcoinclient.kafka;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceChanged(Long id, BigDecimal value, LocalDateTime timestamp) {
}
