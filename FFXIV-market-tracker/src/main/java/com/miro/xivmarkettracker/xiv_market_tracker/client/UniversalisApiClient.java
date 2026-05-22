package com.miro.xivmarkettracker.xiv_market_tracker.client;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.UniversalisApiItemResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UniversalisApiClient {

    private final WebClient universalisapiClient;

    public UniversalisApiClient(WebClient universalisapiClient) {this.universalisapiClient = universalisapiClient;}

    public UniversalisApiItemResponse getItem(String world, Long itemId){
        return universalisapiClient.get()
                .uri("/{world}/{itemId}", world, itemId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new RuntimeException("Item not found: "+ itemId)))
                        .bodyToMono(UniversalisApiItemResponse.class)
                .block();
    }


}
