package com.epam.esm.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;

@Getter
@Setter
public class InvalidInputException extends RuntimeException{
    private BindingResult bindingResult;
    public InvalidInputException(BindingResult br){
        this.bindingResult = br;
    }
}
