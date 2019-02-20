package com.mycompany.bitcoinclient.bus;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PriceMessage {

    private Long id;
    private BigDecimal value;
    private LocalDateTime timestamp;

}
