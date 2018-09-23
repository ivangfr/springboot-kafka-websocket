package com.mycompany.bitcoinapi.bus;

import com.mycompany.bitcoinapi.model.PriceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(Source.class)
public class PriceStream {

    private final Source source;

    public PriceStream(Source source) {
        this.source = source;
    }

    public void sendPrice(PriceEvent priceEvent) {
        Message<String> message = MessageBuilder.withPayload(priceEvent.toString())
                .setHeader("partitionKey", priceEvent.getTimestamp().getTime())
                .build();
        source.output().send(message);

        log.info("{} sent to bus.", priceEvent);
    }

}
