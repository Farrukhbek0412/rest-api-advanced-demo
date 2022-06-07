package com.epam.esm.service.order;

import com.epam.esm.dto.response.OrderGetResponse;
import com.epam.esm.dto.request.OrderPostRequest;
import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.OrderEntity;
import com.epam.esm.entity.UserEntity;
import com.epam.esm.exception.DataAlreadyExistException;
import com.epam.esm.exception.DataNotFoundException;
import com.epam.esm.exception.gift_certificate.InvalidCertificateException;
import com.epam.esm.repository.gift_certificate.GiftCertificateRepository;
import com.epam.esm.repository.order.OrderRepository;
import com.epam.esm.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;

    @Override
    @Transactional
    public OrderGetResponse create(OrderPostRequest orderPostRequest) {
        validator(orderPostRequest);
        GiftCertificateEntity certificateEntity = giftCertificateRepository
                .findById(orderPostRequest.getCertificateId()).get();
        UserEntity user = userRepository.findById(orderPostRequest.getUserId()).get();
        OrderEntity order =
                new OrderEntity(certificateEntity.getPrice(), certificateEntity, user);
        OrderEntity orderEntity = orderRepository.create(order);
        return modelMapper.map(orderEntity, OrderGetResponse.class);
    }

    @Override
    public OrderGetResponse get(Long id) {
        Optional<OrderEntity> order = orderRepository.findById(id);
        return order.map(orderEntity -> modelMapper.map(orderEntity, OrderGetResponse.class))
                .orElseThrow(() -> new DataNotFoundException("order ( id = " + id + " ) not found"));

    }

    @Override
    public List<OrderGetResponse> getOrdersByUserId(Long userId, int pageSize, int pageNo) {
        if (pageNo < 1 || pageSize < 0) {
            throw new InvalidCertificateException("Please enter valid number");
        }
        List<OrderEntity> orders = orderRepository.getOrdersByUserId(userId, pageSize, pageNo);
        if (orders.isEmpty()) {
            throw new DataNotFoundException("no orders found for this user with id: " + userId);
        }
        return modelMapper.map(orders, new TypeToken<List<OrderGetResponse>>() {
        }.getType());
    }

    @Override
    public OrderGetResponse getByUserIdAndOrderId(Long userId, Long orderId) {
        Optional<OrderEntity> order = orderRepository.getByUserIdAndOrderId(userId, orderId);
        return order.map(orderEntity -> modelMapper.map(orderEntity, OrderGetResponse.class))
                .orElseThrow(() ->
                        new DataNotFoundException("no order found with id: " + orderId
                                + " for this user"));
    }

    @Override
    public List<OrderGetResponse> getByCertificateId(Long certificateId, int pageSize, int pageNo) {
        if (pageNo < 1 || pageSize < 0) {
            throw new InvalidCertificateException("Please enter valid number");
        }
        List<OrderEntity> ordersForCertificate = orderRepository.getByCertificateId(certificateId, pageSize, pageNo);
        if (ordersForCertificate.isEmpty()) {
            throw new DataNotFoundException("Orders for certificate ( id = " + certificateId + " ) not found");
        }
        return modelMapper.map(ordersForCertificate, new TypeToken<List<OrderGetResponse>>() {
        }.getType());
    }


    @Override
    public void validator(OrderPostRequest orderPostRequest) {
        if (userRepository.findById(orderPostRequest.getUserId()).isEmpty()) {
            throw new DataNotFoundException(
                    "there is not user with id: " + orderPostRequest.getUserId() + " for this order");
        }
        if (giftCertificateRepository.findById(orderPostRequest.getCertificateId()).isEmpty()) {
            throw new DataNotFoundException(
                    "ordered certificate with id: " + orderPostRequest.getCertificateId() + " does not exist");
        }
        if (orderRepository.getByUserIdAndCertificateId(
                orderPostRequest.getUserId(), orderPostRequest.getCertificateId()).isPresent()) {
            throw new DataAlreadyExistException("this user already ordered this type of certificate");
        }
    }
}
