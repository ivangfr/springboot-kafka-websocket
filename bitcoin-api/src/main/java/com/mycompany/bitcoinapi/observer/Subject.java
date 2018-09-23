package com.mycompany.bitcoinapi.observer;

public interface Subject<T> {

    void register(T observer);

    void unregister(T observer);

    void notifyObservers();

}
