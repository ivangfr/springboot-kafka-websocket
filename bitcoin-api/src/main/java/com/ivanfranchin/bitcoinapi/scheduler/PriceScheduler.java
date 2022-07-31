package com.ivanfranchin.bitcoinapi.scheduler;

import com.ivanfranchin.bitcoinapi.kafka.PriceStreamer;
import com.ivanfranchin.bitcoinapi.model.Price;
import com.ivanfranchin.bitcoinapi.service.PriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Component
public class PriceScheduler {

    private final PriceService priceService;
    private final PriceStreamer priceStreamer;

    @Scheduled(cron = "*/5 * * * * *") // every 5 seconds
    public void streamNewPrice() {
        if (hasTrade()) {
            Price price = priceService.getLastPrice();
            price = getNewPrice(price.getValue());
            priceService.savePrice(price);
            priceStreamer.send(price);
        }
    }

    private boolean hasTrade() {
        return rand.nextBoolean();
    }

    private Price getNewPrice(BigDecimal currentPrice) {
        boolean sign = rand.nextBoolean();
        double var = rand.nextDouble() * 100;
        BigDecimal variation = BigDecimal.valueOf(sign ? var : -1 * var);
        BigDecimal newValue = currentPrice.add(variation).setScale(2, RoundingMode.HALF_UP);
        return new Price(newValue, LocalDateTime.now());
    }

    private static final Random rand = new SecureRandom();
}
