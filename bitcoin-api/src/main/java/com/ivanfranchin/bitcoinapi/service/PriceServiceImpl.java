package com.ivanfranchin.bitcoinapi.service;

import com.ivanfranchin.bitcoinapi.model.Price;
import com.ivanfranchin.bitcoinapi.repository.PriceRepository;
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
