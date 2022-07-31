package com.ivanfranchin.bitcoinapi.mapper;

import com.ivanfranchin.bitcoinapi.kafka.PriceMessage;
import com.ivanfranchin.bitcoinapi.model.Price;
import com.ivanfranchin.bitcoinapi.rest.dto.PriceResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    PriceResponse toPriceResponse(Price price);

    PriceMessage toPriceMessage(Price price);
}
