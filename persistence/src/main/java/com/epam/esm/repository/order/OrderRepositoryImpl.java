package com.epam.esm.repository.order;

import com.epam.esm.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public OrderEntity create(OrderEntity order) {
        entityManager.persist(order);
        long id = order.getId();
        if (id != 0)
            return order;
        return null;
    }

    @Override
    public List<OrderEntity> getAll(int pageSize, int pageNo) {
        return entityManager.createQuery(GET_ALL_ORDERS, OrderEntity.class)
                .setFirstResult((pageNo - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Optional<OrderEntity> findById(Long orderId) {
        OrderEntity orderEntity = entityManager.find(OrderEntity.class, orderId);
        return Optional.ofNullable(orderEntity);
    }


    @Override
    public List<OrderEntity> getOrdersByUserId(Long userId, int pageSize, int pageNo) {
        return entityManager
                .createQuery(GET_ORDER_BY_USER_ID, OrderEntity.class)
                .setParameter("id", userId)
                .setFirstResult((pageNo - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Optional<OrderEntity> getByUserIdAndOrderId(Long userId, Long orderId) {
        List<OrderEntity> orders = entityManager
                .createQuery(
                        GET_ORDER_BY_USER_ID_AND_ORDER_ID, OrderEntity.class)
                .setParameter("userId", userId)
                .setParameter("orderId", orderId)
                .getResultList();

        if (orders.isEmpty())
            return Optional.empty();
        return Optional.of(orders.get(0));
    }

    @Override
    public List<OrderEntity> getByCertificateId(Long certificateId, int pageSize, int pageNo) {
        return entityManager
                .createQuery(GET_ORDERS_BY_CERTIFICATE_ID, OrderEntity.class)
                .setParameter("id", certificateId)
                .setFirstResult((pageNo - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Optional<OrderEntity> getByUserIdAndCertificateId(Long userId, Long certificateId) {
        List<OrderEntity> resultList
                = entityManager.createQuery(
                        GET_ORDER_BY_USER_ID_AND_CERTIFICATE_ID,
                        OrderEntity.class)
                .setParameter("certificateId", certificateId)
                .setParameter("userId", userId)
                .getResultList();

        if (resultList.isEmpty())
            return Optional.empty();
        return Optional.of(resultList.get(0));
    }
}
