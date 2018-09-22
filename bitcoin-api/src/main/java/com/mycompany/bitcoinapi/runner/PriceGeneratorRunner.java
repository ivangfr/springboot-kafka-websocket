package com.mycompany.bitcoinapi.runner;

import com.mycompany.bitcoinapi.model.BitcoinPrice;
import com.mycompany.bitcoinapi.service.BitcoinPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Random;

@Slf4j
@Component
public class PriceGeneratorRunner implements CommandLineRunner {

    private final static int PERIOD_IN_MINUTES = 1;
    private final static BigDecimal OPEN_VALUE = new BigDecimal(6000);

    private final static Random rand = new Random();

    private final BitcoinPriceService bitcoinPriceService;

    public PriceGeneratorRunner(BitcoinPriceService bitcoinPriceService) {
        this.bitcoinPriceService = bitcoinPriceService;
    }

    @Override
    public void run(String... args) throws InterruptedException {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(PERIOD_IN_MINUTES);

        BitcoinPrice bitcoinPrice = new BitcoinPrice(OPEN_VALUE);

        while (true) {
            BigDecimal priceVariation = getPriceVariation();
            bitcoinPrice.setLast(bitcoinPrice.getLast().add(priceVariation));

            log.info("price = {} variation = {} ", bitcoinPrice, priceVariation);
            Thread.sleep(5000);

            LocalDateTime now = LocalDateTime.now();
            if (now.isEqual(end) || now.isAfter(end)) {
                final BitcoinPrice finalBitcoinPrice = bitcoinPrice;
                finalBitcoinPrice.setOpenTime(convertLocalDateTimeToDate(start));
                finalBitcoinPrice.setCloseTime(convertLocalDateTimeToDate(end));
                finalBitcoinPrice.setClose(bitcoinPrice.getLast());

                new Thread(() -> bitcoinPriceService.savePrice(finalBitcoinPrice)).start();

                log.info("closed = {}", bitcoinPrice);

                start = end;
                end = end.plusMinutes(PERIOD_IN_MINUTES);

                bitcoinPrice = new BitcoinPrice(bitcoinPrice.getLast());
            }
        }
    }

    private BigDecimal getPriceVariation() {
        boolean sign = rand.nextBoolean();
        double var = rand.nextDouble() * 100;
        return new BigDecimal(sign ? var : -1 * var).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private Date convertLocalDateTimeToDate(LocalDateTime ldt) {
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        return Date.from(zdt.toInstant());
    }

}
