package com.mycompany.bitcoinapi.service;

import com.mycompany.bitcoinapi.dto.Period;
import com.mycompany.bitcoinapi.model.BitcoinPrice;
import com.mycompany.bitcoinapi.repository.BitcoinPriceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BitcoinPriceServiceImpl implements BitcoinPriceService {

    private final BitcoinPriceRepository bitcoinPriceRepository;

    public BitcoinPriceServiceImpl(BitcoinPriceRepository bitcoinPriceRepository) {
        this.bitcoinPriceRepository = bitcoinPriceRepository;
    }

    @Override
    public BitcoinPrice getLastPrice() {
        return bitcoinPriceRepository.findTopByOrderByOpenTimeDesc();
    }

    @Override
    public Page<BitcoinPrice> listProductsByPage(Period period, Pageable pageable) {
        return bitcoinPriceRepository.findByOpenTimeBetween(period.getFrom(), period.getTo(), pageable);
    }

    @Override
    public BitcoinPrice savePrice(BitcoinPrice bitcoinPrice) {
        return bitcoinPriceRepository.save(bitcoinPrice);
    }
}
