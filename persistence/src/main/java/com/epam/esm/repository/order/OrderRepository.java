package com.epam.esm.repository.order;

import com.epam.esm.entity.OrderEntity;
import com.epam.esm.repository.CRUDRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CRUDRepository<OrderEntity, Long>, OrderQueries{

    List<OrderEntity> getOrdersByUserId(Long userId, int pageSize, int pageNo);

    Optional<OrderEntity> getByUserIdAndOrderId(Long userId, Long orderId);

    Optional<OrderEntity> getByUserIdAndCertificateId(Long userId, Long certificateId);

    List<OrderEntity> getByCertificateId(Long certificateId, int pageSize, int pageNo);
}
