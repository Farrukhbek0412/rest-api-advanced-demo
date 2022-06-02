package com.epam.esm.controller;

import com.epam.esm.dto.BaseResponse;
import com.epam.esm.dto.response.OrderGetResponse;
import com.epam.esm.dto.request.OrderPostRequest;
import com.epam.esm.exception.InvalidInputException;
import com.epam.esm.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/order/")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping(value = "/create",produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<OrderGetResponse>> create(
            @Valid @RequestBody OrderPostRequest orderPostRequest,
            BindingResult bindingResult
            ){
        if(bindingResult.hasErrors())
            throw new InvalidInputException(bindingResult);
        OrderGetResponse response = orderService.create(orderPostRequest);
        return ResponseEntity.status(201)
        .body(new BaseResponse<>(
                201, "certificate ordered", response));
    }

    @GetMapping(value = "/get/by_user",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<List<OrderGetResponse>>> getOrdersByUser(
            @RequestParam Long id,
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "0") int offset
    ){
        List<OrderGetResponse> responses = orderService.getOrdersByUserId(id, limit, offset);
        return ResponseEntity.ok(new BaseResponse<>(200, "user orders", responses));
    }

    @GetMapping(value = "/get/by_certificate",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseResponse<List<OrderGetResponse>>> getOrdersByCertificate(
            @RequestParam Long id,
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "0") int offset
    ){
        List<OrderGetResponse> certificateOrders = orderService.getByCertificateId(id, limit, offset);
        return ResponseEntity.ok(new BaseResponse<>(200, "orders for certificate", certificateOrders));
    }

    @GetMapping(value = "/get/by_user_order")
    public ResponseEntity<BaseResponse<OrderGetResponse>> getOrder(
            @RequestParam Long userId,
            @RequestParam Long orderId
    ){
        OrderGetResponse order = orderService.getByUserIdAndOrderId(userId, orderId);
        return ResponseEntity.ok(new BaseResponse<>(200, "order retrieved", order));
    }
}

