package com.study.ecommerce.domain.type;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum OrderStatus implements Serializable {

    CANCELED,
    ORDER_COMPLETE, //주문완료
    PROCESSING, //출고중
    SHIPPED, //출고완료
    IN_DELIVERY, // 배송중
    DELIVERED, //배송완료

    //환불?


}
