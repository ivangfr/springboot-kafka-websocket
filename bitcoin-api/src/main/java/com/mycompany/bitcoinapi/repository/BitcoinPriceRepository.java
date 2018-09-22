package com.mycompany.bitcoinapi.repository;

import com.mycompany.bitcoinapi.model.BitcoinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface BitcoinPriceRepository extends CrudRepository<BitcoinPrice, Long> {

    BitcoinPrice findTopByOrderByOpenTimeDesc();

    Page<BitcoinPrice> findByOpenTimeBetween(Date from, Date to, Pageable pageable);

}
