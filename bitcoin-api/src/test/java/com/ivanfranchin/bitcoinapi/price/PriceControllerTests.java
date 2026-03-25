package com.ivanfranchin.bitcoinapi.price;

import com.ivanfranchin.bitcoinapi.price.model.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PriceController.class)
class PriceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PriceService priceService;

    @Test
    void testGetLastPriceReturnsLatestBitcoinPrice() throws Exception {
        LocalDateTime timestamp = LocalDateTime.of(2025, 1, 15, 10, 30, 0);
        Price price = new Price(BigDecimal.valueOf(42000.50), timestamp);
        given(priceService.getLastPrice()).willReturn(price);

        mockMvc.perform(get("/api/bitcoin/last"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value", is(42000.50)))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
