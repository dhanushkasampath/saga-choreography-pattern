package com.learn.payment.service.impl;

import com.learn.commons.dto.OrderRequestDto;
import com.learn.commons.dto.PaymentRequestDto;
import com.learn.commons.event.OrderEvent;
import com.learn.commons.event.PaymentEvent;
import com.learn.commons.event.PaymentStatus;
import com.learn.payment.entity.UserBalance;
import com.learn.payment.entity.UserTransaction;
import com.learn.payment.repository.UserBalanceRepository;
import com.learn.payment.repository.UserTransactionRepository;
import com.learn.payment.service.PaymentService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final UserBalanceRepository userBalanceRepository;
    private final UserTransactionRepository userTransactionRepository;

    public PaymentServiceImpl(UserBalanceRepository userBalanceRepository, UserTransactionRepository userTransactionRepository) {
        this.userBalanceRepository = userBalanceRepository;
        this.userTransactionRepository = userTransactionRepository;
    }

    //At the application start up these records will be saved in userBalance table
    @PostConstruct
    public void initUserBalanceInDB(){
        userBalanceRepository.saveAll(Stream.of(
                new UserBalance(102, 3000),
                new UserBalance(103, 4200),
                new UserBalance(104, 20000),
                new UserBalance(105, 999)
        ).collect(Collectors.toList()));
    }

    // get the user id
    // check the balance availability
    // if balance sufficient -> payment completed and deduct amount from db
    // if balance not sufficient -> cancel order event and update the amount in DB
    @Transactional
    @Override
    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
        OrderRequestDto orderRequestDto = orderEvent.getOrderRequestDto();
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(orderRequestDto.getOrderId(),
                orderRequestDto.getUserId(), orderRequestDto.getAmount());

        return userBalanceRepository.findById(orderRequestDto.getUserId())
                .filter(ub -> ub.getPrice() > orderRequestDto.getAmount())
                .map(ub -> {
                    ub.setPrice(ub.getPrice()  - orderRequestDto.getAmount());
                    userTransactionRepository.save(new UserTransaction(orderRequestDto.getOrderId(),
                            orderRequestDto.getUserId(), orderRequestDto.getAmount()));
                    return new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_COMPLETED);
                }).orElse(new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_FAILED));
    }

    @Transactional
    @Override
    public void cancelOrderEvent(OrderEvent orderEvent) {
        userTransactionRepository.findById(orderEvent.getOrderRequestDto().getOrderId())
            .ifPresent(ut -> {
                userTransactionRepository.delete(ut);
                userTransactionRepository.findById(ut.getUserId())
                    .ifPresent(ub -> ub.setAmount(ub.getAmount() + ut.getAmount()));
            });
    }
}
