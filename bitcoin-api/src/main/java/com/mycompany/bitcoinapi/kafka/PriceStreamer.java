package com.mycompany.bitcoinapi.kafka;

import com.mycompany.bitcoinapi.mapper.PriceMapper;
import com.mycompany.bitcoinapi.model.Price;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PriceStreamer {

    private final PriceMapper priceMapper;
    private final StreamBridge streamBridge;

    public void send(Price price) {
        PriceMessage priceMessage = priceMapper.toPriceMessage(price);
        streamBridge.send("prices-out-0", priceMessage);
        log.info("{} sent to bus.", priceMessage);
    }
}
