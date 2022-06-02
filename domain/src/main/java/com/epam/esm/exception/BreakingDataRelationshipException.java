package com.epam.esm.exception;

public class BreakingDataRelationshipException extends RuntimeException {
    public BreakingDataRelationshipException(String message){
        super(message);
    }
}
