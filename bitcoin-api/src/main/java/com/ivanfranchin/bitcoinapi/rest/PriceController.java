package com.ivanfranchin.bitcoinapi.rest;

import com.ivanfranchin.bitcoinapi.model.Price;
import com.ivanfranchin.bitcoinapi.rest.dto.PriceResponse;
import com.ivanfranchin.bitcoinapi.service.PriceService;
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

    @Operation(summary = "Get last price")
    @GetMapping("/last")
    public PriceResponse getLastPrice() {
        Price price = priceService.getLastPrice();
        return new PriceResponse(price.getValue(), price.getTimestamp());
    }
}
