package com.mycompany.bitcoinapi.observer;

public interface Subject {

    void register(PriceObserver observer);

    void unregister(PriceObserver observer);

    void notifyObservers();

}
