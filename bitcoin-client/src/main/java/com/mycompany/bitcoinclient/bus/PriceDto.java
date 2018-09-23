package com.mycompany.bitcoinclient.bus;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PriceDto {

    private BigDecimal value;
    private Date timestamp;

}
