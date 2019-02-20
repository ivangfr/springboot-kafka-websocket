package com.mycompany.bitcoinapi.observer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(Source.class)
public class StreamPriceObserver implements PriceObserver {

    private final Source source;

    public StreamPriceObserver(Source source) {
        this.source = source;
    }

    @Override
    public void update(PriceMessage priceMessage) {
        Message<PriceMessage> message = MessageBuilder.withPayload(priceMessage)
                .setHeader("partitionKey", priceMessage.getId())
                .build();
        source.output().send(message);

        log.info("{} sent to bus.", message);
    }
}
