package com.ivanfranchin.bitcoinapi.price;

import com.ivanfranchin.bitcoinapi.price.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PriceServiceTests {

    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private PriceService priceService;

    @Test
    void testGetLastPriceReturnsMostRecentPrice() {
        LocalDateTime now = LocalDateTime.of(2025, 1, 15, 10, 30, 0);
        Price expected = new Price(BigDecimal.valueOf(42000), now);
        given(priceRepository.findTopByOrderByTimestampDesc()).willReturn(expected);

        Price result = priceService.getLastPrice();

        assertThat(result).isSameAs(expected);
        then(priceRepository).should().findTopByOrderByTimestampDesc();
    }

    @Test
    void testSavePricePersistsAndReturnsPrice() {
        LocalDateTime now = LocalDateTime.of(2025, 1, 15, 10, 30, 0);
        Price price = new Price(BigDecimal.valueOf(37000), now);
        given(priceRepository.save(price)).willReturn(price);

        Price result = priceService.savePrice(price);

        assertThat(result).isSameAs(price);
        then(priceRepository).should().save(price);
    }
}
