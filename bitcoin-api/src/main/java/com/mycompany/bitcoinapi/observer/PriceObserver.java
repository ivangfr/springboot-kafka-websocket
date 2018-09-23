package com.mycompany.bitcoinapi.observer;

import com.mycompany.bitcoinapi.dto.PriceDto;

public interface PriceObserver {

    void update(PriceDto priceDto);

}
