package com.mycompany.bitcoinapi.runner;

import com.mycompany.bitcoinapi.mapper.PriceMapper;
import com.mycompany.bitcoinapi.observer.PriceObserver;
import com.mycompany.bitcoinapi.observer.PriceSubjectRunnable;
import com.mycompany.bitcoinapi.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SimulationRunner implements CommandLineRunner {

    private final PriceService priceService;
    private final PriceMapper priceMapper;
    private final PriceObserver priceObserver;

    @Override
    public void run(String... args) {
        PriceSubjectRunnable priceSubjectRunnable = new PriceSubjectRunnable(priceService, priceMapper);
        priceSubjectRunnable.register(priceObserver);

        new Thread(priceSubjectRunnable).start();
    }
}
