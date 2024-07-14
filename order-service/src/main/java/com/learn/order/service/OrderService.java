package com.learn.order.service;

import com.learn.commons.dto.OrderRequestDto;
import com.learn.order.entity.PurchaseOrder;

import java.util.List;

public interface OrderService {
    PurchaseOrder createOrder(OrderRequestDto orderRequestDto);
    List<PurchaseOrder> getAllOrders();
}
