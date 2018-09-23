package com.mycompany.bitcoinapi.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class TradeServiceImpl implements TradeService {

    private static final int TRADE_DIFFICULT = 4;
    private static final Random rand = new Random();

    private BigDecimal price = new BigDecimal(6000);

    @Override
    public boolean hasTrade() {
        boolean hasTrade = simulateTrade();
        if (hasTrade) {
            price = getNewPrice(price);
        }
        return hasTrade;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
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

    private BigDecimal getNewPrice(BigDecimal price) {
        boolean sign = rand.nextBoolean();
        double var = rand.nextDouble() * 100;
        BigDecimal variation = new BigDecimal(sign ? var : -1 * var);
        return price.add(variation).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
