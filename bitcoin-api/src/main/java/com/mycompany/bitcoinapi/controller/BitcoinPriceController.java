package com.mycompany.bitcoinapi.controller;

import com.mycompany.bitcoinapi.dto.Period;
import com.mycompany.bitcoinapi.model.BitcoinPrice;
import com.mycompany.bitcoinapi.service.BitcoinPriceService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/bitcoin")
public class BitcoinPriceController {

    private final BitcoinPriceService bitcoinPriceService;

    public BitcoinPriceController(BitcoinPriceService bitcoinPriceService) {
        this.bitcoinPriceService = bitcoinPriceService;
    }

    @ApiOperation("Get last price")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/last")
    public BitcoinPrice getLastPrice() {
        return bitcoinPriceService.getLastPrice();
    }

    @ApiOperation(
            value = "Get prices",
            notes = "To sort the results by a specified field, use in 'sort' field a string like: fieldname,[asc|desc]"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/period")
    public Page<BitcoinPrice> getPrices(@Valid @RequestBody Period period, Pageable pageable) {
        return bitcoinPriceService.listProductsByPage(period, pageable);
    }

}
