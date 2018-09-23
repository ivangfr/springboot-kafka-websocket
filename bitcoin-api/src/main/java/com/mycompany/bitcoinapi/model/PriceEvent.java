package com.mycompany.bitcoinapi.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PriceEvent {

    private final BigDecimal price;
    private final Date timestamp;

}
