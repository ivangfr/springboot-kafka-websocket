package com.ivanfranchin.bitcoinapi.price;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import com.ivanfranchin.bitcoinapi.price.event.PriceChanged;
import com.ivanfranchin.bitcoinapi.price.model.Price;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class PriceEventEmitter {

  private static final String BINDING_NAME = "prices-out-0";

  private final StreamBridge streamBridge;

  public void send(Price price) {
    PriceChanged priceMessage =
        new PriceChanged(price.getId(), price.getValue(), price.getTimestamp());
    streamBridge.send(BINDING_NAME, priceMessage);
    log.info("{} sent to bus.", priceMessage);
  }
}
