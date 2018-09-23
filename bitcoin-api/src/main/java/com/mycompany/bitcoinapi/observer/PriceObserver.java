package com.mycompany.bitcoinapi.observer;

public interface PriceObserver<T> {

    void update(T message);

}
