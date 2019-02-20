package com.mycompany.bitcoinapi.observer;

import com.mycompany.bitcoinapi.model.Price;
import com.mycompany.bitcoinapi.service.PriceService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class PriceSubjectRunnable implements PriceSubject, Runnable {

    private static final int SLEEP_TIME = 1000;
    private static final int TRADE_DIFFICULT = 4;
    private static final Random rand = new Random();
    private static final BigDecimal INITIAL_PRICE = new BigDecimal(6000);

    private List<PriceObserver> observers = new ArrayList<>();

    private Price price = new Price(INITIAL_PRICE, LocalDateTime.now());

    private final PriceService priceService;
    private final MapperFacade mapperFacade;

    public PriceSubjectRunnable(PriceService priceService, MapperFacade mapperFacade) {
        this.priceService = priceService;
        this.mapperFacade = mapperFacade;
    }

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
                e.printStackTrace();
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
        PriceMessage priceMessage = mapperFacade.map(price, PriceMessage.class);
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
        BigDecimal variation = new BigDecimal(sign ? var : -1 * var);
        BigDecimal newValue = currentPrice.add(variation).setScale(2, BigDecimal.ROUND_HALF_UP);
        return new Price(newValue, LocalDateTime.now());
    }

}
