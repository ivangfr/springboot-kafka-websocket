package com.mycompany.bitcoinclient.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class PriceController {

    @GetMapping("/")
    public String getPrices() {
        return "prices";
    }

}
