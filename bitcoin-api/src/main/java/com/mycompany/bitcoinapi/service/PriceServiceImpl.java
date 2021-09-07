package com.mycompany.bitcoinapi.service;

import com.mycompany.bitcoinapi.model.Price;
import com.mycompany.bitcoinapi.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    @Override
    public Price getLastPrice() {
        return priceRepository.findTopByOrderByTimestampDesc();
    }

    @Override
    public Price savePrice(Price price) {
        return priceRepository.save(price);
    }
}
