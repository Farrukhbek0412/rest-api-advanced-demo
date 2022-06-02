package com.epam.esm.controller;

import com.epam.esm.dto.BaseResponse;
import com.epam.esm.dto.response.UserGetResponse;
import com.epam.esm.dto.request.UserPostRequest;
import com.epam.esm.exception.InvalidInputException;
import com.epam.esm.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/v1/user/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/create",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<BaseResponse<UserGetResponse>> create(
            @Valid @RequestBody UserPostRequest userPostRequest,
            BindingResult bindingResult){
        if(bindingResult.hasErrors())
            throw new InvalidInputException(bindingResult);
        UserGetResponse createdUser = userService.create(userPostRequest);
        createdUser.add(linkTo(methodOn(OrderController.class)
                .getOrdersByUser(createdUser.getId(), 50, 0)).withRel("user orders"));
        return ResponseEntity.status(201).body(new BaseResponse<>(200, "user created", createdUser));
    }

    @GetMapping(value = "/get",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<UserGetResponse>> get(
            @RequestParam Long id
    ){
        UserGetResponse response = userService.get(id);
        response.add(linkTo(methodOn(OrderController.class)
                .getOrdersByUser(response.getId(), 50, 0)).withRel("user orders"));
        return ResponseEntity.ok().body(new BaseResponse<>(200, "user details", response));
    }

    @GetMapping(value = "/get_all",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<List<UserGetResponse>>> getAll(
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset
    ){
        List<UserGetResponse> userList = userService.getAll(limit, offset);
        userList.forEach(user -> {
            user.add(linkTo(methodOn(OrderController.class)
                    .getOrdersByUser(user.getId(), 50, 0)).withRel("user orders"));
        });
        return ResponseEntity.ok(new BaseResponse<>(200, "users list", userList));
    }
}