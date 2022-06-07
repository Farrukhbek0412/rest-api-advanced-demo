package com.epam.esm.repository.order;

import com.epam.esm.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.NoSuchElementException;
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
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderEntity> query = builder.createQuery(OrderEntity.class);
        Root<OrderEntity> root = query.from(OrderEntity.class);
        query.select(root);

        return entityManager.createQuery(query)
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

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderEntity> query = builder.createQuery(OrderEntity.class);
        Root<OrderEntity> root = query.from(OrderEntity.class);
        root.fetch("user", JoinType.LEFT);
        query.where(builder.equal(root.get("user").get("id"), userId));

        return entityManager
                .createQuery(query)
                .setFirstResult((pageNo - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Optional<OrderEntity> getByUserIdAndOrderId(Long userId, Long orderId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderEntity> query = builder.createQuery(OrderEntity.class);
        Root<OrderEntity> root = query.from(OrderEntity.class);
        query.where(builder.equal(root.get("user").get("id"), userId),
                builder.equal(root.get("id"), orderId));

        Optional<OrderEntity> orders;

        try {
            orders = Optional.ofNullable(entityManager
                    .createQuery(query)
                    .getSingleResult());
            return orders;
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OrderEntity> getByCertificateId(Long certificateId, int pageSize, int pageNo) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderEntity> query = builder.createQuery(OrderEntity.class);
        Root<OrderEntity> root = query.from(OrderEntity.class);
        root.fetch("certificate", JoinType.LEFT);
        query.where(builder.equal(root.get("certificate").get("id"), certificateId));

        return entityManager
                .createQuery(query)
                .setFirstResult((pageNo - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Optional<OrderEntity> getByUserIdAndCertificateId(Long userId, Long certificateId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderEntity> query = builder.createQuery(OrderEntity.class);
        Root<OrderEntity> root = query.from(OrderEntity.class);
        query.where(
                builder.equal(root.get("user").get("id"), userId),
                builder.equal(root.get("certificate").get("id"), certificateId));

        List<OrderEntity> resultList = entityManager.createQuery(query)
                .getResultList();
        if (resultList.isEmpty())
            return Optional.empty();
        return Optional.of(resultList.get(0));
    }
}
