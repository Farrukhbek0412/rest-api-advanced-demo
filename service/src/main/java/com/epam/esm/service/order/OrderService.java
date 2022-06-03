package com.epam.esm.service.order;

import com.epam.esm.dto.response.OrderGetResponse;
import com.epam.esm.dto.request.OrderPostRequest;
import com.epam.esm.service.BaseService;

import java.util.List;

public interface OrderService extends BaseService<OrderPostRequest, OrderGetResponse> {

    List<OrderGetResponse> getOrdersByUserId(Long userId, int pageSize, int pageNo);

    OrderGetResponse getByUserIdAndOrderId(Long userId, Long orderId);

    List<OrderGetResponse> getByCertificateId(Long certificateId, int pageSize, int pageNo);

    void validator(OrderPostRequest orderPostRequest);

}
