package com.learn.order.service.impl;

import com.learn.commons.dto.OrderRequestDto;
import com.learn.commons.event.OrderStatus;
import com.learn.order.entity.PurchaseOrder;
import com.learn.order.repository.OrderRepository;
import com.learn.order.service.OrderService;
import com.learn.order.service.OrderStatusPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusPublisher orderStatusPublisher;

    public OrderServiceImpl(OrderRepository orderRepository, OrderStatusPublisher orderStatusPublisher) {
        this.orderRepository = orderRepository;
        this.orderStatusPublisher = orderStatusPublisher;
    }

    @Transactional
    @Override
    public PurchaseOrder createOrder(OrderRequestDto orderRequestDto) {
        // persist the order details in the orderDB
        PurchaseOrder order =  orderRepository.save(convertDtoToEntity(orderRequestDto));
        orderRequestDto.setOrderId(order.getId());
        // immediately produce kafka event with status ORDER_CREATED
        orderStatusPublisher.publishOrderEvent(orderRequestDto, OrderStatus.ORDER_CREATED);
        return order;
    }

    public List<PurchaseOrder> getAllOrders(){
        return orderRepository.findAll();
    }

    private PurchaseOrder convertDtoToEntity(OrderRequestDto orderRequestDto){
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setProductId(orderRequestDto.getProductId());
        purchaseOrder.setUserId(orderRequestDto.getUserId());
        purchaseOrder.setOrderStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(orderRequestDto.getAmount());
        return purchaseOrder;
    }
}
