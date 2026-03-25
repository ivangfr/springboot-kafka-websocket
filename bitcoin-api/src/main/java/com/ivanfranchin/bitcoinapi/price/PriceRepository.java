package com.ivanfranchin.bitcoinapi.price;

import com.ivanfranchin.bitcoinapi.price.model.Price;
import org.springframework.data.repository.CrudRepository;

public interface PriceRepository extends CrudRepository<Price, Long> {

    Price findTopByOrderByTimestampDesc();
}
