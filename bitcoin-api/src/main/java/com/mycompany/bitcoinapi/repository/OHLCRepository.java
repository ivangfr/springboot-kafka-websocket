package com.mycompany.bitcoinapi.repository;

import com.mycompany.bitcoinapi.model.OHLC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface OHLCRepository extends CrudRepository<OHLC, Long> {

    Page<OHLC> findByOpenTimeBetween(Date from, Date to, Pageable pageable);

}
