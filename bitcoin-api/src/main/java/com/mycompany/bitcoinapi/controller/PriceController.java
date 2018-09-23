package com.mycompany.bitcoinapi.controller;

import com.mycompany.bitcoinapi.dto.OHLCDto;
import com.mycompany.bitcoinapi.dto.PeriodRequest;
import com.mycompany.bitcoinapi.dto.PriceDto;
import com.mycompany.bitcoinapi.service.OHLCService;
import com.mycompany.bitcoinapi.service.PriceService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ma.glasnost.orika.MapperFacade;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bitcoin")
public class PriceController {

    private final PriceService priceService;
    private final OHLCService ohlcPriceService;
    private final MapperFacade mapperFacade;

    public PriceController(PriceService priceService, OHLCService ohlcPriceService, MapperFacade mapperFacade) {
        this.priceService = priceService;
        this.ohlcPriceService = ohlcPriceService;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation("Get last price")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/last")
    public PriceDto getLastPrice() {
        return new PriceDto(priceService.getLastPrice().getValue(), new DateTime(DateTimeZone.UTC).toDate());
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
    public List<OHLCDto> getOHLCs(@Valid @RequestBody PeriodRequest periodRequest) {
        return ohlcPriceService.listOHLCByPage(periodRequest.getFrom(), periodRequest.getTo())
                .stream()
                .map(ohlc -> mapperFacade.map(ohlc, OHLCDto.class))
                .collect(Collectors.toList());
    }

}
