package com.mycompany.bitcoinapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class BitcoinApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitcoinApiApplication.class, args);
    }
}
