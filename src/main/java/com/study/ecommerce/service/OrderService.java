package com.study.ecommerce.service;

import com.study.ecommerce.config.CustomUserDetails;
import com.study.ecommerce.domain.*;
import com.study.ecommerce.domain.type.OrderStatus;
import com.study.ecommerce.exception.CartEmptyException;
import com.study.ecommerce.exception.NotFoundMemberException;
import com.study.ecommerce.exception.NotFoundOrderException;
import com.study.ecommerce.repository.CartRepository;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.repository.OrderProductRepository;
import com.study.ecommerce.repository.OrderRepository;
import com.study.ecommerce.request.OrderRequest;
import com.study.ecommerce.response.OrderProductResponse;
import com.study.ecommerce.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ClientInfoStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final CartRepository cartRepository;

    private final MemberRepository memberRepository;

    private final OrderProductRepository orderProductRepository;

    @Transactional(readOnly = true)
    public List<OrderResponse> findAllOrders(Pageable pageable){

        Member member = memberRepository.findByEmail(getMemberEmail()).orElseThrow(NotFoundMemberException::new);

        return orderProductFindByOrderId(orderRepository.findByMember(member,pageable));
    }



    //기간 조회
    @Transactional(readOnly = true)
    public List<OrderResponse> findOrderDate(Pageable pageable, LocalDate startDate, LocalDate endDate){

        Member member = memberRepository.findByEmail(getMemberEmail()).orElseThrow(NotFoundMemberException::new);

        //LocalDate -> LocalDateTime으로 변경해주기
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23,59,59);

        return orderProductFindByOrderId(orderRepository.findByMemberAndCreatedDateBetween(member,pageable,startDateTime,endDateTime));
    }

    //주문 취소
    @Transactional
    public void orderCancel(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(NotFoundOrderException::new);

        if (!Objects.equals(order.getMember().getEmail(), getMemberEmail())){
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        order.setOrderModify(OrderStatus.CANCELED);

        orderRepository.save(order);
    }

    //주문 생성
    @Transactional
    public void createOrder(OrderRequest orderRequest){

        Member member = memberRepository.findByEmail(getMemberEmail()).orElseThrow(NotFoundMemberException::new);

        List<Cart> carts = cartRepository.findAllByMember(member);

        if (carts.isEmpty()){
            throw new CartEmptyException();
        }

        Order saveOrder = Order.builder()
                .orderStatus(OrderStatus.ORDER_COMPLETE)
                .address(orderRequest.getAddress())
                .payment(orderRequest.getPayment())
                .member(member)
                .totalPrice(orderRequest.getTotalPrice())
                .build();


        orderRepository.save(saveOrder);

        carts.forEach(cart -> {
            orderProductRepository.save(OrderProduct.builder()
                            .orderId(saveOrder.getId())
                            .product(cart.getCartProduct())
                            .quantity(cart.getQuantity())
                    .build());
        });


    }

    //관리자용
    @Transactional
    public void modifyOrder(OrderRequest orderRequest) throws Exception {
       Order order = orderRepository.findById(orderRequest.getOrderId()).orElseThrow(NotFoundOrderException::new);

       Member findMember = memberRepository.findByEmail(getMemberEmail()).orElseThrow(NotFoundMemberException::new);

       if (!findMember.getRole().equals("ROLE_ADMIN")){
           throw new IllegalAccessException();
       }


       order.setOrderModify(orderRequest.getOrderStatus());

       orderRepository.save(order);
    }



    public String getMemberEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private List<OrderResponse> orderProductFindByOrderId(Page<Order> orders){
        List<OrderResponse> orderResponseList = new ArrayList<>();

        for (Order order : orders){
            OrderResponse orderResponse = OrderResponse.form(order);

            List<OrderProduct> orderProductList = orderProductRepository.findByOrderId(order.getId());

            for (OrderProduct orderProduct : orderProductList){
                OrderProductResponse orderProductResponse = OrderProductResponse.form(orderProduct);

                orderResponse.addOrderProductResponse(orderProductResponse);
            }

            orderResponseList.add(orderResponse);

        }

        return  orderResponseList;

    }


}
