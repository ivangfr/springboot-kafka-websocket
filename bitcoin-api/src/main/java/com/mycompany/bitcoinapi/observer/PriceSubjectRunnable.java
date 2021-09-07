package com.mycompany.bitcoinapi.observer;

import com.mycompany.bitcoinapi.mapper.PriceMapper;
import com.mycompany.bitcoinapi.model.Price;
import com.mycompany.bitcoinapi.service.PriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
public class PriceSubjectRunnable implements PriceSubject, Runnable {

    private static final int SLEEP_TIME = 1000;
    private static final int TRADE_DIFFICULT = 4;
    private static final Random rand = new SecureRandom();
    private static final BigDecimal INITIAL_PRICE = new BigDecimal(6000);

    private final List<PriceObserver> observers = new ArrayList<>();

    private Price price = new Price(INITIAL_PRICE, LocalDateTime.now());

    private final PriceService priceService;
    private final PriceMapper priceMapper;

    @Override
    public void run() {
        priceService.savePrice(price);

        while (true) {
            try {
                Thread.sleep(SLEEP_TIME);

                boolean hasTrade = simulateTrade();
                if (hasTrade) {
                    price = getNewPrice(price.getValue());
                    priceService.savePrice(price);
                    notifyObservers();
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void register(PriceObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void unregister(PriceObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        PriceMessage priceMessage = priceMapper.toPriceMessage(price);
        log.info("New {}", priceMessage);
        observers.forEach(observer -> observer.update(priceMessage));
    }

    private boolean simulateTrade() {
        boolean trade = true;
        int i = 0;
        do {
            trade = trade && rand.nextBoolean();
            ++i;
        } while (i < TRADE_DIFFICULT);
        return trade;
    }

    private Price getNewPrice(BigDecimal currentPrice) {
        boolean sign = rand.nextBoolean();
        double var = rand.nextDouble() * 100;
        BigDecimal variation = BigDecimal.valueOf(sign ? var : -1 * var);
        BigDecimal newValue = currentPrice.add(variation).setScale(2, RoundingMode.HALF_UP);
        return new Price(newValue, LocalDateTime.now());
    }
}
