package com.mycompany.bitcoinapi.service;

import com.mycompany.bitcoinapi.dto.Period;
import com.mycompany.bitcoinapi.model.BitcoinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BitcoinPriceService {

    BitcoinPrice getLastPrice();

    Page<BitcoinPrice> listProductsByPage(Period period, Pageable pageable);

    BitcoinPrice savePrice(BitcoinPrice bitcoinPrice);
}
