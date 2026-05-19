package com.miro.xivmarkettracker.xiv_market_tracker.client;

import com.miro.xivmarkettracker.xiv_market_tracker.DTO.XivApiItemResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class XivApiClient {
    private final WebClient xivapiClient;

    public XivApiClient(WebClient xivapiClient){
        this.xivapiClient = xivapiClient;
    }

    public Mono<XivApiItemResponse> getItem(Integer itemId){
        return xivapiClient.get()
                .uri("/item/{id}",itemId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,response ->
                        Mono.error(new RuntimeException("Item not found: "+ itemId)))
                .bodyToMono(XivApiItemResponse.class);
    }
}
