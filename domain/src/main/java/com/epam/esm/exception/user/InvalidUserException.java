
package com.epam.esm.exception.user;

public class InvalidUserException extends RuntimeException{

    public InvalidUserException(String message){
        super(message);
    }
}
