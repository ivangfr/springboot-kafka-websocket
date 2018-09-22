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
public class BitcoinPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal open;    // Open price
    private BigDecimal close;   // Close price
    private BigDecimal high;    // High price
    private BigDecimal low;     // Low price
    private BigDecimal last;    // Last price
    private Date openTime;      // Open date and time
    private Date closeTime;     // Close date and time

    public BitcoinPrice(BigDecimal price) {
        this.open = price;
        this.high = price;
        this.low = price;
        this.last = price;
    }

    public void setLast(BigDecimal last) {
        this.last = last;
        if (last.compareTo(this.high) > 0) {
            this.high = last;
        }
        if (last.compareTo(this.low) < 0) {
            this.low = last;
        }
    }
}
