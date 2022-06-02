package com.epam.esm.exception;

public class DataAlreadyExistException extends RuntimeException{

    public DataAlreadyExistException(String message){
        super(message);
    }
}
