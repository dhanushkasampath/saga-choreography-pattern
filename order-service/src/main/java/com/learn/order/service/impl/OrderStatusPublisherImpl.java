package com.learn.order.service.impl;

import com.learn.commons.dto.OrderRequestDto;
import com.learn.commons.event.OrderEvent;
import com.learn.commons.event.OrderStatus;
import com.learn.order.service.OrderStatusPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class OrderStatusPublisherImpl implements OrderStatusPublisher {

    private final Sinks.Many<OrderEvent> orderSinks;

    public OrderStatusPublisherImpl(Sinks.Many<OrderEvent> orderSinks) {
        this.orderSinks = orderSinks;
    }

    public void publishOrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus){
        OrderEvent orderEvent = new OrderEvent(orderRequestDto, orderStatus);
        orderSinks.tryEmitNext(orderEvent);
    }
}
