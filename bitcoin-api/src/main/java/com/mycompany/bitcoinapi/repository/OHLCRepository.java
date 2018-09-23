package com.mycompany.bitcoinapi.repository;

import com.mycompany.bitcoinapi.model.OHLC;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface OHLCRepository extends CrudRepository<OHLC, Long> {

    List<OHLC> findByOpenTimeBetween(Date from, Date to);

}
