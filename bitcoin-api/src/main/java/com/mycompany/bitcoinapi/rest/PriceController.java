package com.mycompany.bitcoinapi.rest;

import com.mycompany.bitcoinapi.rest.dto.PriceDto;
import com.mycompany.bitcoinapi.service.PriceService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bitcoin")
public class PriceController {

    private final PriceService priceService;
    private final MapperFacade mapperFacade;

    public PriceController(PriceService priceService, MapperFacade mapperFacade) {
        this.priceService = priceService;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation("Get last price")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("/last")
    public PriceDto getLastPrice() {
        return mapperFacade.map(priceService.getLastPrice(), PriceDto.class);
    }

}
