package com.mycompany.bitcoinapi.observer;

import com.mycompany.bitcoinapi.dto.PriceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(Source.class)
public class PriceStreamObserver implements PriceObserver {

    private final Source source;

    public PriceStreamObserver(Source source) {
        this.source = source;
    }

    @Override
    public void update(PriceDto priceDto) {
        Message<String> message = MessageBuilder.withPayload(priceDto.toString())
                .setHeader("partitionKey", priceDto.getTimestamp().getTime())
                .build();
        source.output().send(message);

        log.info("{} sent to bus.", priceDto);
    }
}
