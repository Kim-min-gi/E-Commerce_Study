package com.study.ecommerce.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressResponse {

    private String city;
    private String street;
    private String zipcode;



}
