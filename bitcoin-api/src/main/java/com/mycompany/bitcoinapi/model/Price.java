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
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal value;
    private Date timestamp;

    public Price(BigDecimal value, Date timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }
}
