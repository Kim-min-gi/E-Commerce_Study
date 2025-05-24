package com.study.ecommerce.domain.type;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public enum Payment implements Serializable {
    @JsonProperty("CARD")
    CARD,

    CASH,

    @JsonProperty("KAKAO_PAY")
    KAKAO_PAY(),

    NAVER_PAY

}
