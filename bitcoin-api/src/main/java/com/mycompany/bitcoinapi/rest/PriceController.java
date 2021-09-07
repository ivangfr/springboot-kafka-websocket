package com.mycompany.bitcoinapi.rest;

import com.mycompany.bitcoinapi.mapper.PriceMapper;
import com.mycompany.bitcoinapi.rest.dto.PriceDto;
import com.mycompany.bitcoinapi.service.PriceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bitcoin")
public class PriceController {

    private final PriceService priceService;
    private final PriceMapper priceMapper;

    @Operation(summary = "Get last price")
    @GetMapping("/last")
    public PriceDto getLastPrice() {
        return priceMapper.toPriceDto(priceService.getLastPrice());
    }
}
