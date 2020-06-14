package com.mycompany.bitcoinapi.mapper;

import com.mycompany.bitcoinapi.model.Price;
import com.mycompany.bitcoinapi.observer.PriceMessage;
import com.mycompany.bitcoinapi.rest.dto.PriceDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    PriceDto toPriceDto(Price price);

    PriceMessage toPriceMessage(Price price);

}
