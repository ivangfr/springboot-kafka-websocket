package com.mycompany.bitcoinapi.runner;

import com.mycompany.bitcoinapi.observer.PriceObserver;
import com.mycompany.bitcoinapi.observer.PriceSubjectRunnable;
import com.mycompany.bitcoinapi.service.PriceService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SimulationRunner implements CommandLineRunner {

    private final PriceService priceService;
    private final MapperFacade mapperFacade;
    private final PriceObserver priceObserver;

    public SimulationRunner(PriceService priceService, MapperFacade mapperFacade, PriceObserver priceObserver) {
        this.priceService = priceService;
        this.mapperFacade = mapperFacade;
        this.priceObserver = priceObserver;
    }

    @Override
    public void run(String... args) {
        PriceSubjectRunnable priceSubjectRunnable = new PriceSubjectRunnable(priceService, mapperFacade);
        priceSubjectRunnable.register(priceObserver);

        new Thread(priceSubjectRunnable).start();
    }

}
