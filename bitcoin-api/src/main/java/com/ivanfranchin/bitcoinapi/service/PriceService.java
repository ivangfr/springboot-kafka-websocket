package com.ivanfranchin.bitcoinapi.service;

import com.ivanfranchin.bitcoinapi.model.Price;

public interface PriceService {

    Price getLastPrice();

    Price savePrice(Price price);
}
