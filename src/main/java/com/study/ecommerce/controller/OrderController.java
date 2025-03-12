package com.study.ecommerce.controller;

import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.Order;
import com.study.ecommerce.request.OrderRequest;
import com.study.ecommerce.response.OrderResponse;
import com.study.ecommerce.service.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> findAllOrders(@PageableDefault Pageable pageable){
        List<OrderResponse> orders = orderService.findAllOrders(pageable);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponse> findOrder(@PathVariable Long orderId) throws IllegalAccessException {
        OrderResponse order = orderService.findOrder(orderId);

        return ResponseEntity.ok(order);
    }


    @GetMapping("/ordersDate")
    public ResponseEntity<List<OrderResponse>> findOrderDate(@PageableDefault Pageable pageable,
                                                             @RequestParam LocalDate startDate,@RequestParam LocalDate endDate){
        List<OrderResponse> orders = orderService.findOrderDate(pageable,startDate,endDate);

        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/orderCancel/{orderId}")
    public ResponseEntity<Void> orderCancel(@PathVariable Long orderId){

        orderService.orderCancel(orderId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/orderCreate")
    public ResponseEntity<Void> createOrder(@RequestBody OrderRequest orderRequest){

        orderService.createOrder(orderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/orderModify")
    public ResponseEntity<Void> modifyOrder(@RequestBody OrderRequest orderRequest) throws Exception {

        orderService.modifyOrder(orderRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
