package com.mycompany.bitcoinapi.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OHLCDto {

    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;
    private Date openTime;
    private Date closeTime;

}
