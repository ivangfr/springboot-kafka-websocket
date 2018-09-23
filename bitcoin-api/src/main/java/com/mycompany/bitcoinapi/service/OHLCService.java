package com.mycompany.bitcoinapi.service;

import com.mycompany.bitcoinapi.dto.Period;
import com.mycompany.bitcoinapi.model.OHLC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OHLCService {

    Page<OHLC> listOHLCByPage(Period period, Pageable pageable);

    OHLC saveOHLC(OHLC ohlc);
}
