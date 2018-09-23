package com.mycompany.bitcoinapi.service;

import com.mycompany.bitcoinapi.model.Price;
import com.mycompany.bitcoinapi.repository.PriceRepository;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    public PriceServiceImpl(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public Price getLastPrice() {
        return priceRepository.findTopByOrderByTimestampDesc();
    }

    @Override
    public Price savePrice(Price price) {
        return priceRepository.save(price);
    }

}
