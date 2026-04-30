package com.ivanfranchin.bitcoinapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

  @Value("${spring.application.name}")
  private String applicationName;

  @Bean
  OpenAPI customOpenAPI() {
    return new OpenAPI().components(new Components()).info(new Info().title(applicationName));
  }
}
