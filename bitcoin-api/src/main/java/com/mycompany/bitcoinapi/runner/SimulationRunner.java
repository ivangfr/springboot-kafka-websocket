package com.mycompany.bitcoinapi.runner;

import com.mycompany.bitcoinapi.observer.PriceObserver;
import com.mycompany.bitcoinapi.observer.PriceSubjectRunnable;
import com.mycompany.bitcoinapi.service.PriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimulationRunner implements CommandLineRunner {

    private final PriceService priceService;
    private final PriceObserver priceObserver;

    public SimulationRunner(PriceService priceService, PriceObserver priceObserver) {
        this.priceService = priceService;
        this.priceObserver = priceObserver;
    }

    @Override
    public void run(String... args) {
        PriceSubjectRunnable priceSubjectRunnable = new PriceSubjectRunnable(priceService);
        priceSubjectRunnable.register(priceObserver);

        new Thread(priceSubjectRunnable).start();
    }

}
