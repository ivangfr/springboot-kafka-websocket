package com.mycompany.bitcoinapi.runner;

import com.mycompany.bitcoinapi.observer.PriceStreamObserver;
import com.mycompany.bitcoinapi.observer.PriceSubjectRunnable;
import com.mycompany.bitcoinapi.observer.PriceSubjectRunnableImpl;
import com.mycompany.bitcoinapi.service.PriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimulationRunner implements CommandLineRunner {

    private final PriceService priceService;
    private final PriceStreamObserver priceStreamObserver;

    public SimulationRunner(PriceService priceService, PriceStreamObserver priceStreamObserver) {
        this.priceService = priceService;
        this.priceStreamObserver = priceStreamObserver;
    }

    @Override
    public void run(String... args) {
        PriceSubjectRunnable priceSubjectRunnable = new PriceSubjectRunnableImpl(priceService);
        priceSubjectRunnable.register(priceStreamObserver);

        new Thread(priceSubjectRunnable).start();
    }

}
