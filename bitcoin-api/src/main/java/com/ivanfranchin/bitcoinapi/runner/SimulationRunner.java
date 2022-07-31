package com.ivanfranchin.bitcoinapi.runner;

import com.ivanfranchin.bitcoinapi.model.Price;
import com.ivanfranchin.bitcoinapi.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class SimulationRunner implements CommandLineRunner {

    private final PriceService priceService;

    @Override
    public void run(String... args) {
        priceService.savePrice(INITIAL_PRICE);
    }

    private static final Price INITIAL_PRICE = new Price(BigDecimal.valueOf(37000), LocalDateTime.now());
}
