package com.learn.order.config;

import com.learn.commons.event.OrderEvent;
import com.learn.commons.event.PaymentEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This class is to consume the payment details in "payment-event" topic
 */
@Configuration
public class EventConsumerConfig {

    private final OrderStatusUpdateHandler orderStatusUpdateHandler;

    public EventConsumerConfig(OrderStatusUpdateHandler orderStatusUpdateHandler) {
        this.orderStatusUpdateHandler = orderStatusUpdateHandler;
    }

    /**
     * this method should follow these steps
     *
     * 1. listen payment-event topic
     * 2. will check payment status
     * 3. if payment status completed -> complete the order
     * 4. if payment status failed -> cancel the order
     * @return
     */
    @Bean
    public Consumer<PaymentEvent> paymentEventConsumer(){
        return (paymentEvent -> orderStatusUpdateHandler.updateOrder(paymentEvent.getPaymentRequestDto().getOrderId(), po -> {
            po.setPaymentStatus(paymentEvent.getPaymentStatus());
        }));
    }
}
