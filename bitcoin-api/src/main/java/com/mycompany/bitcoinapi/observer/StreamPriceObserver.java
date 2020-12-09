package com.mycompany.bitcoinapi.observer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StreamPriceObserver implements PriceObserver {

    private final StreamBridge streamBridge;

    @Override
    public void update(PriceMessage priceMessage) {
        Message<PriceMessage> message = MessageBuilder.withPayload(priceMessage)
                .setHeader("partitionKey", priceMessage.getId())
                .build();
        streamBridge.send("prices-out-0", message);

        log.info("{} sent to bus.", message);
    }
}
