package com.miro.xivmarkettracker.xiv_market_tracker.exceptions;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException (String message){
        super(message);
    }
}
