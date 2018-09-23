package com.mycompany.bitcoinapi.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PriceDto {

    private final BigDecimal price;
    private final Date timestamp;

}
