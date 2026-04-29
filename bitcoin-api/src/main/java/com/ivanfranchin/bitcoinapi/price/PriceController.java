package com.ivanfranchin.bitcoinapi.price;

import com.ivanfranchin.bitcoinapi.price.dto.PriceResponse;
import com.ivanfranchin.bitcoinapi.price.model.Price;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bitcoin")
public class PriceController {

    private final PriceService priceService;

    @GetMapping("/last")
    public ResponseEntity<PriceResponse> getLastPrice() {
        Price price = priceService.getLastPrice();
        if (price == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(PriceResponse.from(price));
    }
}
