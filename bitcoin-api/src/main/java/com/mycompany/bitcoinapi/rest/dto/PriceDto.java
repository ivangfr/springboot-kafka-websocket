package com.mycompany.bitcoinapi.rest.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
public class PriceDto {

    BigDecimal value;
    LocalDateTime timestamp;
}
