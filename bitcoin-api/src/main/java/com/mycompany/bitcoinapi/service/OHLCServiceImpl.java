package com.mycompany.bitcoinapi.service;

import com.mycompany.bitcoinapi.dto.Period;
import com.mycompany.bitcoinapi.model.OHLC;
import com.mycompany.bitcoinapi.repository.OHLCRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OHLCServiceImpl implements OHLCService {

    private final OHLCRepository ohlcRepository;

    public OHLCServiceImpl(OHLCRepository ohlcRepository) {
        this.ohlcRepository = ohlcRepository;
    }

    @Override
    public Page<OHLC> listOHLCByPage(Period period, Pageable pageable) {
        return ohlcRepository.findByOpenTimeBetween(period.getFrom(), period.getTo(), pageable);
    }

    @Override
    public OHLC saveOHLC(OHLC ohlc) {
        return ohlcRepository.save(ohlc);
    }
}
