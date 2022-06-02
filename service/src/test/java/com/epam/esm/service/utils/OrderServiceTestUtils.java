package com.epam.esm.service.utils;


import com.epam.esm.dto.response.OrderGetResponse;
import com.epam.esm.dto.request.OrderPostRequest;
import com.epam.esm.entity.OrderEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.service.utils.GiftCertificateServiceTestUtils.getGiftCertificateEntity;
import static com.epam.esm.service.utils.UserServiceTestUtils.getUser;

public class OrderServiceTestUtils {

    public static List<OrderEntity> getOrders() {
        OrderEntity order = new OrderEntity();
        order.setId(1L);
        order.setPrice(new BigDecimal("10.2"));
        order.setCreateDate(LocalDateTime.parse("2020-04-04T10:00:00"));
        order.setCertificate(getGiftCertificateEntity());
        order.setUser(getUser());

        OrderEntity order1 = new OrderEntity();
        order1.setId(2L);
        order1.setPrice(new BigDecimal("1000"));
        order1.setCreateDate(LocalDateTime.parse("2020-02-04T11:26:00"));
        order1.setCertificate(getGiftCertificateEntity());
        order1.setUser(getUser());

        OrderEntity order2 = new OrderEntity();
        order2.setId(2L);
        order2.setPrice(new BigDecimal("10.1"));
        order2.setCreateDate(LocalDateTime.parse("2020-04-04T10:00:00"));
        order2.setCertificate(getGiftCertificateEntity());
        order2.setUser(getUser());

        List<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.add(order);
        orderEntities.add(order1);
        orderEntities.add(order2);
        return orderEntities;
    }

    public static List<OrderGetResponse> getOrderList() {
        OrderGetResponse order = new OrderGetResponse();
        order.setId(1L);
        order.setPrice(new BigDecimal("10.2"));
        order.setCreateDate(LocalDateTime.parse("2020-04-04T10:00:00"));
        order.setCertificate(getGiftCertificateEntity());
        order.setUser(getUser());

        OrderGetResponse order1 = new OrderGetResponse();
        order1.setId(2L);
        order1.setPrice(new BigDecimal("100"));
        order1.setCreateDate(LocalDateTime.parse("2020-02-04T11:26:00"));
        order1.setCertificate(getGiftCertificateEntity());
        order1.setUser(getUser());

        OrderGetResponse order2 = new OrderGetResponse();
        order.setId(2L);
        order.setPrice(new BigDecimal("10.1"));
        order.setCreateDate(LocalDateTime.parse("2020-04-04T10:00:00"));
        order.setCertificate(getGiftCertificateEntity());
        order.setUser(getUser());

        List<OrderGetResponse> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order1);
        orders.add(order2);
        return orders;
    }

    public static OrderPostRequest getOrderPostRequest() {
        return new OrderPostRequest(12L, 10L);
    }

    public static OrderEntity getOrderEntity() {
        OrderEntity order = new OrderEntity();
        order.setId(2L);
        order.setPrice(new BigDecimal("10.1"));
        order.setCreateDate(LocalDateTime.parse("2020-04-04T10:00:00"));
        order.setCertificate(getGiftCertificateEntity());
        order.setUser(getUser());
        return order;
    }

    public static OrderGetResponse getOrderResponse(){
        OrderGetResponse order = new OrderGetResponse();
        order.setId(1L);
        order.setPrice(new BigDecimal("10.2"));
        order.setCreateDate(LocalDateTime.parse("2020-04-04T10:00:00"));
        order.setCertificate(getGiftCertificateEntity());
        order.setUser(getUser());
        return order;
    }
}
