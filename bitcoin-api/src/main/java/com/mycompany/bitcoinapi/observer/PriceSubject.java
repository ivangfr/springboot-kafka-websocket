package com.mycompany.bitcoinapi.observer;

public interface PriceSubject {

    void register(PriceObserver priceObserver);

    void unregister(PriceObserver priceObserver);

    void notifyObservers();
}
