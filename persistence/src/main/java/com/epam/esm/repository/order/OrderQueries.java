package com.epam.esm.repository.order;

public interface OrderQueries {

    String GET_ALL_ORDERS = "select o from OrderEntity o";
    String GET_ORDER_BY_USER_ID = "select o from OrderEntity o where o.user.id = :id";
    String GET_ORDER_BY_USER_ID_AND_ORDER_ID
            = "select o from OrderEntity o where o.user.id = :userId and o.id = :orderId";
    String GET_ORDERS_BY_CERTIFICATE_ID = "select o from OrderEntity o where o.certificate.id = :id";
    String GET_ORDER_BY_USER_ID_AND_CERTIFICATE_ID
            = "select o from OrderEntity o where o.certificate.id = :certificateId and o.user.id = :userId";
}
