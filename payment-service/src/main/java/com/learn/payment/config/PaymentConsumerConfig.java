package com.learn.payment.config;

import com.learn.commons.event.OrderEvent;
import com.learn.commons.event.OrderStatus;
import com.learn.commons.event.PaymentEvent;
import com.learn.payment.service.PaymentService;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * consumers the order-event and publish the payment-event
 */
@Configuration
public class PaymentConsumerConfig {

    private final PaymentService paymentService;

    public PaymentConsumerConfig(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Bean
    public Sinks.Many<OrderEvent> orderSinks(){
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    /**
     * Function is a functional interface which came from java 8
     * it consumers the order-event topic and publish to the payment-event topic
     * @param
     * @return
     */
    @Bean
    public Function<Flux<OrderEvent>, Flux<PaymentEvent>> paymentProcessor(){
        return orderEventFlux -> orderEventFlux.flatMap(this::processPayment);
    }

    private Mono<PaymentEvent> processPayment(OrderEvent orderEvent) {
        // get the user id
        // check the balance availability
        // if balance sufficient -> payment completed and deduct amount from db
        // if balance not sufficient -> cancel order event and update the amount in DB
        if (OrderStatus.ORDER_CREATED.equals(orderEvent.getOrderStatus())) {
            return Mono.fromSupplier(() -> this.paymentService.newOrderEvent(orderEvent));
        } else {
            return Mono.fromRunnable(() -> this.paymentService.cancelOrderEvent(orderEvent));
        }
    }
}
