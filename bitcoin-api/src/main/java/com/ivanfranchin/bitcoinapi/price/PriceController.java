package com.ivanfranchin.bitcoinapi.price;

import com.ivanfranchin.bitcoinapi.price.dto.PriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bitcoin")
public class PriceController {

    private final PriceService priceService;

    @GetMapping("/last")
    public PriceResponse getLastPrice() {
        return PriceResponse.from(priceService.getLastPrice());
    }
}
