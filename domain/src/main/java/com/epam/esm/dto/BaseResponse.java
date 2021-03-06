package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;

public class BaseResponse<T> extends RepresentationModel<BaseResponse<T>> {

    private int status;
    private String message;
    private T data;

    public BaseResponse() {

    }

    public BaseResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
