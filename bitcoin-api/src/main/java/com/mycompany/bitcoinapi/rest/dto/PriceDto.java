package com.mycompany.bitcoinapi.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PriceDto {

    private BigDecimal value;
    private LocalDateTime timestamp;

}
