package com.ivanfranchin.bitcoinapi.price;

import org.springframework.data.repository.CrudRepository;

import com.ivanfranchin.bitcoinapi.price.model.Price;

public interface PriceRepository extends CrudRepository<Price, Long> {

  Price findTopByOrderByTimestampDesc();
}
