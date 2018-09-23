package com.mycompany.bitcoinapi.runner;

import com.mycompany.bitcoinapi.observer.OHLCObserver;
import com.mycompany.bitcoinapi.observer.OHLCObserverImpl;
import com.mycompany.bitcoinapi.observer.PriceStreamObserver;
import com.mycompany.bitcoinapi.observer.PriceSubject;
import com.mycompany.bitcoinapi.observer.PriceSubjectImpl;
import com.mycompany.bitcoinapi.service.OHLCService;
import com.mycompany.bitcoinapi.service.PriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimulationRunner implements CommandLineRunner {

    private final PriceService priceService;
    private final OHLCService ohlcService;
    private final PriceStreamObserver priceStream;

    public SimulationRunner(PriceService priceService, OHLCService ohlcService, PriceStreamObserver priceStream) {
        this.priceService = priceService;
        this.ohlcService = ohlcService;
        this.priceStream = priceStream;
    }

    @Override
    public void run(String... args) {
        PriceSubject priceSubject = new PriceSubjectImpl(priceService);

        OHLCObserver ohlcObserver = new OHLCObserverImpl(priceSubject, ohlcService);

        priceSubject.register(ohlcObserver);
        priceSubject.register(priceStream);

        new Thread(priceSubject).start();
        new Thread(ohlcObserver).start();
    }

}
