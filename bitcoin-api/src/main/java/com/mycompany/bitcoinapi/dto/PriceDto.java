package com.mycompany.bitcoinapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PriceDto {

    private BigDecimal value;
    private Date timestamp;

}
