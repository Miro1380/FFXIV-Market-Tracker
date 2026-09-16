package com.miro.xivmarkettracker.xiv_market_tracker.exceptions;

public class DuplicateUserException extends RuntimeException{
    public DuplicateUserException (String message){
        super(message);
    }
}
