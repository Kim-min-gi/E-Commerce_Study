package com.study.ecommerce.domain.type;

public enum OrderStatus {

    CANCELED,
    ORDER_COMPLETE, //주문완료
    PROCESSING, //출고중
    SHIPPED, //출고완료
    IN_DELIVERY, // 배송중
    DELIVERED, //배송완료

    //환불?


}
