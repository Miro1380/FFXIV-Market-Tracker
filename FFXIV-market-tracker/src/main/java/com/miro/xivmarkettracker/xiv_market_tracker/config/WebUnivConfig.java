package com.miro.xivmarkettracker.xiv_market_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebUnivConfig {
    @Bean
    public WebClient universalisapiClient(){
        return WebClient.builder()
                .baseUrl("https://universalis.app/api/v2")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
