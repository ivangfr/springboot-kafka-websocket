package com.mycompany.bitcoinapi.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
public class OHLC {

    // OHLC = Open-High-Low-Close

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;
    private Date openTime;
    private Date closeTime;

    public OHLC(Date openTime, BigDecimal price) {
        this.openTime = openTime;
        this.open = price;
        this.high = price;
        this.low = price;
    }

}
