package com.learn.commons.event;

import com.learn.commons.dto.OrderRequestDto;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
public class OrderEvent implements Event {

    private UUID eventId = UUID.randomUUID();
    private Date eventDate = new Date();
    private OrderRequestDto orderRequestDto;
    private OrderStatus orderStatus;

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Date getEventDate() {
        return eventDate;
    }

    public OrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus) {
        this.orderRequestDto = orderRequestDto;
        this.orderStatus = orderStatus;
    }
}
