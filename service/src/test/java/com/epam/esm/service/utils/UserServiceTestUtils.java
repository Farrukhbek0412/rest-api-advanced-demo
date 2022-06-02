package com.epam.esm.service.utils;

import com.epam.esm.dto.response.UserGetResponse;
import com.epam.esm.dto.request.UserPostRequest;
import com.epam.esm.entity.UserEntity;

public class UserServiceTestUtils {

    public static UserPostRequest getUserPostRequest() {
        UserPostRequest userPostRequest = new UserPostRequest();
        userPostRequest.setName("Farrux");
        userPostRequest.setAge("20");
        userPostRequest.setEmail("email");
        userPostRequest.setPassword("password");
        return userPostRequest;
    }

    public static UserEntity getUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Farrux");
        userEntity.setAge(20);
        userEntity.setEmail("email");
        userEntity.setPassword("password");
        return userEntity;
    }

    public static UserGetResponse getUserResponse() {
        UserGetResponse userGetResponse = new UserGetResponse();
        userGetResponse.setName("Farrux");
        userGetResponse.setAge(20);
        userGetResponse.setEmail("email");
        userGetResponse.setPassword("password");
        return userGetResponse;
    }
}
