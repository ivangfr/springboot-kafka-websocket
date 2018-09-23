package com.mycompany.bitcoinapi.observer;

import com.mycompany.bitcoinapi.dto.PriceDto;
import com.mycompany.bitcoinapi.model.OHLC;
import com.mycompany.bitcoinapi.service.OHLCService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
public class OHLCObserverImpl implements OHLCObserver {

    private OHLC ohlc;
    private BigDecimal bdPrice;

    private final PriceSubject priceSubject;
    private final OHLCService ohlcService;

    public OHLCObserverImpl(PriceSubject priceSubject, OHLCService ohlcService) {
        this.priceSubject = priceSubject;
        this.ohlcService = ohlcService;
    }

    @Override
    public void run() {
        bdPrice = priceSubject.getPrice().getValue();

        DateTime openTime = new DateTime(DateTimeZone.UTC);
        DateTime closeTime = openTime.plusMinutes(Ticker.PERIOD_IN_MINUTES).withSecondOfMinute(0).withMillisOfSecond(0);

        ohlc = new OHLC(openTime.toDate(), bdPrice);
        while (true) {
            try {
                Thread.sleep(Ticker.SLEEP_TIME);

                DateTime now = new DateTime(DateTimeZone.UTC);
                if (now.isEqual(closeTime) || now.isAfter(closeTime)) {
                    closeOHLC(ohlc, bdPrice, closeTime.toDate());

                    openTime = now;
                    closeTime = openTime.plusMinutes(Ticker.PERIOD_IN_MINUTES).withSecondOfMinute(0).withMillisOfSecond(0);

                    ohlc = new OHLC(openTime.toDate(), bdPrice);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(PriceDto priceDto) {
        bdPrice = priceDto.getValue();
        if (bdPrice.compareTo(ohlc.getHigh()) > 0) {
            ohlc.setHigh(bdPrice);
        }
        if (bdPrice.compareTo(ohlc.getLow()) < 0) {
            ohlc.setLow(bdPrice);
        }
        log.info("bdPrice = {} bitcoin_price = {}", bdPrice, ohlc);
    }

    private void closeOHLC(OHLC ohlc, BigDecimal close, Date closeTime) {
        new Thread(() -> {
            ohlc.setClose(close);
            ohlc.setCloseTime(closeTime);
            OHLC ohlcSaved = ohlcService.saveOHLC(ohlc);
            log.info("OHLC closed = {}", ohlcSaved);
        }).start();
    }

}
