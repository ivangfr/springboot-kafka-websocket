package com.ivanfranchin.bitcoinapi.price;

import com.ivanfranchin.bitcoinapi.price.model.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PriceService {

    private final PriceRepository priceRepository;

    public Price getLastPrice() {
        return priceRepository.findTopByOrderByTimestampDesc();
    }

    public Price savePrice(Price price) {
        return priceRepository.save(price);
    }
}
