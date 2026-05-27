package com.miro.xivmarkettracker.xiv_market_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class XivMarketTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(XivMarketTrackerApplication.class, args);
	}

}
