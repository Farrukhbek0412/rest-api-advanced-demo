package com.epam.esm.controller;

import com.epam.esm.dto.BaseResponse;
import com.epam.esm.dto.response.OrderGetResponse;
import com.epam.esm.dto.request.OrderPostRequest;
import com.epam.esm.exception.InvalidInputException;
import com.epam.esm.exception.gift_certificate.InvalidCertificateException;
import com.epam.esm.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/order/")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping(value = "/create", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<OrderGetResponse>> create(
            @Valid @RequestBody OrderPostRequest orderPostRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors())
            throw new InvalidInputException(bindingResult);
        OrderGetResponse response = orderService.create(orderPostRequest);
        return ResponseEntity.status(201)
                .body(new BaseResponse<>(
                        HttpStatus.CREATED.value(), "certificate ordered", response));
    }

    @GetMapping(value = "/get/by-user",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<List<OrderGetResponse>>> getOrdersByUser(
            @RequestParam Long id,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "1") int pageNo
    ) {
        if (pageNo < 1 || pageSize < 0)
            throw new InvalidCertificateException("Please enter valid number");
        List<OrderGetResponse> responses = orderService.getOrdersByUserId(id, pageSize, pageNo);
        BaseResponse<List<OrderGetResponse>> response = new BaseResponse<>(
                HttpStatus.OK.value(), "user orders", responses);

        if (!responses.isEmpty())
            response.add(linkTo(methodOn(OrderController.class)
                    .getOrdersByUser(id, pageSize, pageNo + 1))
                    .withRel("next page"));

        if (pageNo > 1) {
            response.add(linkTo(methodOn(OrderController.class)
                    .getOrdersByUser(id, pageSize, pageNo - 1))
                    .withRel("previous page"));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/get/by-certificate",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<List<OrderGetResponse>>> getOrdersByCertificate(
            @RequestParam Long id,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "1") int pageNo
    ) {

        if (pageNo < 1 || pageSize < 0)
            throw new InvalidCertificateException("Please enter valid number");
        List<OrderGetResponse> certificateOrders = orderService.getByCertificateId(id, pageSize, pageNo);

        BaseResponse<List<OrderGetResponse>> response = new BaseResponse<>(
                HttpStatus.OK.value(), "user orders", certificateOrders);

        if (!certificateOrders.isEmpty())
            response.add(linkTo(methodOn(OrderController.class)
                    .getOrdersByCertificate(id, pageSize, pageNo + 1))
                    .withRel("next page"));

        if (pageNo > 1) {
            response.add(linkTo(methodOn(OrderController.class)
                    .getOrdersByCertificate(id, pageSize, pageNo - 1))
                    .withRel("previous page"));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/get/by-user-order")
    public ResponseEntity<BaseResponse<OrderGetResponse>> getOrder(
            @RequestParam Long userId,
            @RequestParam Long orderId
    ) {
        OrderGetResponse order = orderService.getByUserIdAndOrderId(userId, orderId);
        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(),
                "order retrieved", order));
    }
}

