package com.learn.order.service;

import com.learn.commons.dto.OrderRequestDto;
import com.learn.commons.event.OrderStatus;

public interface OrderStatusPublisher {
    void publishOrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus);
}
