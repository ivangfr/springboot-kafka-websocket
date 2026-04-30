package com.ivanfranchin.bitcoinapi.price;

import org.springframework.stereotype.Service;

import com.ivanfranchin.bitcoinapi.price.model.Price;

import lombok.RequiredArgsConstructor;

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
