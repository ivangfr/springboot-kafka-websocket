package com.mycompany.bitcoinapi.service;

import com.mycompany.bitcoinapi.model.Price;

public interface PriceService {

    Price getLastPrice();

    Price savePrice(Price price);
}
