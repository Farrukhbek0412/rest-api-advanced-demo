package com.epam.esm.resources;


import com.epam.esm.dto.response.UserGetResponse;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class UserResource extends RepresentationModel<UserResource> {
    private final UserGetResponse user;
    public UserResource(final UserGetResponse user) {
        this.user = user;
    }

}
