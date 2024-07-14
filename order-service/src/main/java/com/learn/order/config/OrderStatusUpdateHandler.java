package com.learn.order.config;

import com.learn.commons.dto.OrderRequestDto;
import com.learn.commons.event.OrderStatus;
import com.learn.commons.event.PaymentStatus;
import com.learn.order.entity.PurchaseOrder;
import com.learn.order.repository.OrderRepository;
import com.learn.order.service.OrderStatusPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Configuration
public class OrderStatusUpdateHandler {

    private final OrderRepository orderRepository;

    private final OrderStatusPublisher orderStatusPublisher;

    public OrderStatusUpdateHandler(OrderRepository orderRepository, OrderStatusPublisher orderStatusPublisher) {
        this.orderRepository = orderRepository;
        this.orderStatusPublisher = orderStatusPublisher;
    }

    @Transactional
    public void updateOrder(int id, Consumer<PurchaseOrder> consumer){
        orderRepository.findById(id)
                .ifPresent(consumer.andThen(this::updateOrder));
    }

    /**
     * In side this updateOrder method check the payment status.
     * if payment-status completed -> mark as completed
     * else cancel the order
     * @param purchaseOrder
     */
    private void updateOrder(PurchaseOrder purchaseOrder) {
        boolean isPaymentComplete = PaymentStatus.PAYMENT_COMPLETED.equals(purchaseOrder.getPaymentStatus());
        OrderStatus orderStatus = isPaymentComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;
        purchaseOrder.setOrderStatus(orderStatus);
        if (!isPaymentComplete) {
            orderStatusPublisher.publishOrderEvent(convertEntityToDto(purchaseOrder), orderStatus);
        }
    }

    private OrderRequestDto convertEntityToDto(PurchaseOrder purchaseOrder) {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setOrderId(purchaseOrder.getId());
        orderRequestDto.setUserId(purchaseOrder.getUserId());
        orderRequestDto.setAmount(purchaseOrder.getPrice());
        orderRequestDto.setProductId(purchaseOrder.getProductId());
        return orderRequestDto;
    }
}
