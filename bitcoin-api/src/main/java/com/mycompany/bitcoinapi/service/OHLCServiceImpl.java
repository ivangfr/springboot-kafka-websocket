package com.mycompany.bitcoinapi.service;

import com.mycompany.bitcoinapi.model.OHLC;
import com.mycompany.bitcoinapi.repository.OHLCRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OHLCServiceImpl implements OHLCService {

    private final OHLCRepository ohlcRepository;

    public OHLCServiceImpl(OHLCRepository ohlcRepository) {
        this.ohlcRepository = ohlcRepository;
    }

    @Override
    public List<OHLC> listOHLCByPage(Date from, Date to) {
        return ohlcRepository.findByOpenTimeBetween(from, to);
    }

    @Override
    public OHLC saveOHLC(OHLC ohlc) {
        return ohlcRepository.save(ohlc);
    }
}
