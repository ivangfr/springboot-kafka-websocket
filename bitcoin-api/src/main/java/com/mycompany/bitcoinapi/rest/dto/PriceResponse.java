package com.mycompany.bitcoinapi.rest.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
public class PriceResponse {

    BigDecimal value;
    LocalDateTime timestamp;
}
