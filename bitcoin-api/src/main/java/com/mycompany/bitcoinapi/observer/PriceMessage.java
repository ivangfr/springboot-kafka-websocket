package com.mycompany.bitcoinapi.observer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PriceMessage {

    private Long id;
    private BigDecimal value;
    private LocalDateTime timestamp;
}
