package com.mycompany.bitcoinapi.observer;

import com.mycompany.bitcoinapi.model.Price;

public interface PriceSubject extends Subject, Runnable {

    Price getPrice();

}
