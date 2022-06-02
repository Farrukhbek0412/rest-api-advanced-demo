package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaseExceptionResponse {
    private int httpStatus;
    private String message;
    private int errorCode;
}
