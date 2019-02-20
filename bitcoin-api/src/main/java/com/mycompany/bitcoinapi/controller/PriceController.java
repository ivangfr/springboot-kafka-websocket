package com.mycompany.bitcoinapi.controller;

import com.mycompany.bitcoinapi.dto.PriceDto;
import com.mycompany.bitcoinapi.service.PriceService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bitcoin")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @ApiOperation("Get last price")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("/last")
    public PriceDto getLastPrice() {
        return new PriceDto(priceService.getLastPrice().getValue(), new DateTime(DateTimeZone.UTC).toDate());
    }

}
