package com.mycompany.bitcoinapi.repository;

import com.mycompany.bitcoinapi.model.Price;
import org.springframework.data.repository.CrudRepository;

public interface PriceRepository extends CrudRepository<Price, Long> {

    Price findTopByOrderByTimestampDesc();

}
