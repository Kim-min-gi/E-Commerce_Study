package com.study.ecommerce.service;

import com.study.ecommerce.domain.type.Payment;
import com.study.ecommerce.request.PaymentRequest;
import com.study.ecommerce.response.PaymentResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final Map<Payment, PaymentStrategy> strategyMap;

    public PaymentService(List<PaymentStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(PaymentStrategy::getPaymentType, Function.identity()));
    }

    public PaymentResponse ready(PaymentRequest context) {
        return strategyMap.get(context.getOrderRequest().getPayment()).ready(context);
    }

    public PaymentResponse approve(PaymentRequest context) throws IllegalAccessException {
        return strategyMap.get(context.getOrderRequest().getPayment()).approve(context);
    }

    public PaymentResponse cancel(PaymentRequest context) throws IllegalAccessException {
        return strategyMap.get(context.getOrderRequest().getPayment()).cancel(context);
    }


}
