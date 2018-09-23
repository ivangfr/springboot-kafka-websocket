package com.mycompany.bitcoinapi.controller;

import com.mycompany.bitcoinapi.dto.PriceDto;
import com.mycompany.bitcoinapi.dto.Period;
import com.mycompany.bitcoinapi.model.OHLC;
import com.mycompany.bitcoinapi.service.OHLCService;
import com.mycompany.bitcoinapi.service.TradeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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

    private final OHLCService ohlcPriceService;
    private final TradeService tradeService;

    public BitcoinPriceController(OHLCService ohlcPriceService, TradeService tradeService) {
        this.ohlcPriceService = ohlcPriceService;
        this.tradeService = tradeService;
    }

    @ApiOperation("Get last price")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/last")
    public PriceDto getLastPrice() {
        return new PriceDto(tradeService.getPrice(), new DateTime(DateTimeZone.UTC).toDate());
    }

    @ApiOperation(
            value = "Get OHLC's",
            notes = "To sort the results by a specified field, use in 'sort' field a string like: fieldname,[asc|desc]"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/period")
    public Page<OHLC> getOHLCs(@Valid @RequestBody Period period, Pageable pageable) {
        return ohlcPriceService.listOHLCByPage(period, pageable);
    }

}
