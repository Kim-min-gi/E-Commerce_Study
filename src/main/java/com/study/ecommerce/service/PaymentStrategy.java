package com.study.ecommerce.service;

import com.study.ecommerce.domain.type.Payment;
import com.study.ecommerce.request.PaymentRequest;
import com.study.ecommerce.response.PaymentResponse;

public interface PaymentStrategy {

  PaymentResponse ready(PaymentRequest paymentRequest);

  PaymentResponse approve(PaymentRequest paymentRequest) throws IllegalAccessException;

  PaymentResponse cancel(PaymentRequest paymentRequest) throws IllegalAccessException;

  Payment getPaymentType();


}
