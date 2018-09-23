package com.mycompany.bitcoinapi.service;

import java.math.BigDecimal;

public interface TradeService {

    boolean hasTrade();

    BigDecimal getPrice();

}
