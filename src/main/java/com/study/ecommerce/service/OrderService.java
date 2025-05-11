package com.study.ecommerce.service;

import com.study.ecommerce.domain.*;
import com.study.ecommerce.domain.type.OrderStatus;
import com.study.ecommerce.exception.*;
import com.study.ecommerce.exception.NotFoundProduct;
import com.study.ecommerce.repository.*;
import com.study.ecommerce.request.OrderItemRequest;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final CartRepository cartRepository;

    private final MemberRepository memberRepository;

    private final OrderProductRepository orderProductRepository;

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<OrderResponse> findAllOrders(Pageable pageable){

        Member member = memberRepository.findByEmail(getMemberEmail()).orElseThrow(NotFoundMemberException::new);

        return orderProductFindByOrderId(orderRepository.findByMember(member,pageable));
    }

    @Transactional(readOnly = true)
    public OrderResponse findOrder(Long orderId) throws IllegalAccessException {

        Member member = memberRepository.findByEmail(getMemberEmail()).orElseThrow(NotFoundMemberException::new);
        Order findOrder = orderRepository.findByOrderId(orderId).orElseThrow(NotFoundOrderException::new);

        if (!member.getRole().equals("ROLE_ADMIN") && !member.getEmail().equals(findOrder.getMember().getEmail())){
            throw new IllegalAccessException("잘못된 접근 입니다.");
        }

        OrderResponse form = OrderResponse.form(findOrder);

        List<OrderProduct> orderProductList = orderProductRepository.findByOrderId(findOrder.getId());

        for (OrderProduct orderProduct : orderProductList){
            OrderProductResponse orderProductResponse = OrderProductResponse.form(orderProduct);

            form.addOrderProductResponse(orderProductResponse);
        }

        return form;
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

        order.setOrderStatusModify(OrderStatus.CANCELED);

        //orderRepository.save(order);
    }

    //주문 생성
    @Transactional
    public void createOrder(OrderRequest orderRequest){

        Member member = memberRepository.findByEmail(getMemberEmail()).orElseThrow(NotFoundMemberException::new);

        long totalPrice = 0;

        ArrayList<OrderProduct> orderProducts = new ArrayList<>();


        //각 상품들을 다시 조회해서 가격을 확인 후 totalPrice 확정내기
        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItemRequestList()){
            Product product = productRepository.findById(orderItemRequest.getProductId()).orElseThrow(NotFoundProduct::new);

            if (product.getPrice() != orderItemRequest.getPrice()){
                throw new IncorrectOrderException();
            }

            OrderProduct orderProduct = OrderProduct.builder()
                    .product(product)
                    .quantity(orderItemRequest.getQuantity())
                    .build();

            orderProducts.add(orderProduct);

            totalPrice += (long) product.getPrice() * orderItemRequest.getQuantity();
        }

        //총 주문금액이 다른경우
        if (totalPrice != orderRequest.getTotalPrice()){
            throw new IncorrectOrderException();
        }

        Order saveOrder = Order.builder()
                .orderStatus(OrderStatus.ORDER_COMPLETE)
                .address(orderRequest.getAddress())
                .payment(orderRequest.getPayment())
                .member(member)
                .totalPrice(totalPrice)
                .build();

        orderRepository.save(saveOrder);

        orderProducts.forEach(saveOrder::addOrderProduct);

        orderProductRepository.saveAll(orderProducts);

        //cart list 삭제 고민

    }

    //관리자용
    @Transactional
    public void modifyOrder(OrderRequest orderRequest) throws Exception {
       Order order = orderRepository.findById(orderRequest.getOrderId()).orElseThrow(NotFoundOrderException::new);

       Member findMember = memberRepository.findByEmail(getMemberEmail()).orElseThrow(NotFoundMemberException::new);

       if (!findMember.getRole().equals("ROLE_ADMIN")){
           throw new IllegalAccessException();
       }


       order.setOrderStatusModify(orderRequest.getOrderStatus());

//       orderRepository.save(order);
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
