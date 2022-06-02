package com.epam.esm.service.order;

import com.epam.esm.dto.response.OrderGetResponse;
import com.epam.esm.dto.request.OrderPostRequest;
import com.epam.esm.service.BaseService;

import java.util.List;

public interface OrderService extends BaseService<OrderPostRequest, OrderGetResponse> {

    List<OrderGetResponse> getOrdersByUserId(Long userId, int limit, int offset);

    OrderGetResponse getByUserIdAndOrderId(Long userId, Long orderId);

    List<OrderGetResponse> getByCertificateId(Long certificateId, int limit, int offset);

    void validator(OrderPostRequest orderPostRequest);

}
