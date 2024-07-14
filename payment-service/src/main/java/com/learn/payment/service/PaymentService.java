package com.learn.payment.service;

import com.learn.commons.event.OrderEvent;
import com.learn.commons.event.PaymentEvent;

public interface PaymentService {
    PaymentEvent newOrderEvent(OrderEvent orderEvent);

    void cancelOrderEvent(OrderEvent orderEvent);
}
