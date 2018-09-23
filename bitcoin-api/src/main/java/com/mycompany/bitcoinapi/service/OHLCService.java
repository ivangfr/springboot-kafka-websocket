package com.mycompany.bitcoinapi.service;

import com.mycompany.bitcoinapi.model.OHLC;

import java.util.Date;
import java.util.List;

public interface OHLCService {

    List<OHLC> listOHLCByPage(Date from, Date to);

    OHLC saveOHLC(OHLC ohlc);
}
