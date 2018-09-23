package com.mycompany.bitcoinapi.runner;

import com.mycompany.bitcoinapi.bus.PriceStream;
import com.mycompany.bitcoinapi.model.OHLC;
import com.mycompany.bitcoinapi.model.PriceEvent;
import com.mycompany.bitcoinapi.service.OHLCService;
import com.mycompany.bitcoinapi.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Component
public class SimulationRunner implements CommandLineRunner {

    private static final int PERIOD_IN_MINUTES = 1;
    private static final int SLEEP_TIME = PERIOD_IN_MINUTES * 1000;

    private final OHLCService ohlcService;
    private final PriceStream priceStream;
    private final TradeService tradeService;

    public SimulationRunner(OHLCService ohlcService, PriceStream priceStream, TradeService tradeService) {
        this.ohlcService = ohlcService;
        this.priceStream = priceStream;
        this.tradeService = tradeService;
    }

    @Override
    public void run(String... args) throws InterruptedException {
        DateTime openTime = new DateTime(DateTimeZone.UTC);
        DateTime closeTime = openTime.plusMinutes(PERIOD_IN_MINUTES).withSecondOfMinute(0).withMillisOfSecond(0);

        BigDecimal price = tradeService.getPrice();
        OHLC ohlc = new OHLC(openTime.toDate(), price);

        while (true) {
            Thread.sleep(SLEEP_TIME);

            if (tradeService.hasTrade()) {
                price = tradeService.getPrice();

                // Send new price to Kafka
                priceStream.sendPrice(new PriceEvent(price, new DateTime(DateTimeZone.UTC).toDate()));

                if (price.compareTo(ohlc.getHigh()) > 0) {
                    ohlc.setHigh(price);
                }
                if (price.compareTo(ohlc.getLow()) < 0) {
                    ohlc.setLow(price);
                }
                log.debug("price = {} bitcoin_price = {}", price, ohlc);
            }

            DateTime now = new DateTime(DateTimeZone.UTC);
            if (now.isEqual(closeTime) || now.isAfter(closeTime)) {
                closeOHLC(ohlc, price, closeTime.toDate());

                openTime = now;
                closeTime = openTime.plusMinutes(PERIOD_IN_MINUTES).withSecondOfMinute(0).withMillisOfSecond(0);

                ohlc = new OHLC(openTime.toDate(), price);
            }
        }
    }

    private void closeOHLC(OHLC ohlc, BigDecimal close, Date closeTime) {
        new Thread(() -> {
            ohlc.setClose(close);
            ohlc.setCloseTime(closeTime);
            OHLC ohlcSaved = ohlcService.saveOHLC(ohlc);
            log.debug("OHLC closed = {}", ohlcSaved);
        }).start();
    }

}
