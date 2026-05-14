package com.ivanfranchin.bitcoinapi.price.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "prices")
public class Price {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private BigDecimal value;

  @Column(nullable = false)
  private LocalDateTime timestamp;

  public Price(BigDecimal value, LocalDateTime timestamp) {
    this.value = value;
    this.timestamp = timestamp;
  }
}
