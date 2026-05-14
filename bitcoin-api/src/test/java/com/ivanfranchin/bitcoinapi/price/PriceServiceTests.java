package com.ivanfranchin.bitcoinapi.price;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ivanfranchin.bitcoinapi.price.model.Price;

@ExtendWith(SpringExtension.class)
@Import(PriceService.class)
class PriceServiceTests {

  @MockitoBean private PriceRepository priceRepository;

  @Autowired private PriceService priceService;

  @Test
  void testGetLastPriceReturnsMostRecentPrice() {
    LocalDateTime now = LocalDateTime.of(2025, 1, 15, 10, 30, 0);
    Price expected = new Price(BigDecimal.valueOf(42000), now);
    when(priceRepository.findTopByOrderByTimestampDesc()).thenReturn(expected);

    Price result = priceService.getLastPrice();

    assertThat(result).isSameAs(expected);
    verify(priceRepository).findTopByOrderByTimestampDesc();
  }

  @Test
  void testSavePricePersistsAndReturnsPrice() {
    LocalDateTime now = LocalDateTime.of(2025, 1, 15, 10, 30, 0);
    Price price = new Price(BigDecimal.valueOf(37000), now);
    when(priceRepository.save(price)).thenReturn(price);

    Price result = priceService.savePrice(price);

    assertThat(result).isSameAs(price);
    verify(priceRepository).save(price);
  }
}
