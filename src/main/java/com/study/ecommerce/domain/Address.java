package com.study.ecommerce.domain;

import com.study.ecommerce.response.AddressResponse;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address implements Serializable {

    private String city;
    private String street;
    private String zipcode;



    public static AddressResponse form(Address address){
        return AddressResponse.builder()
                .city(address.getCity())
                .street(address.getStreet())
                .zipcode(address.getZipcode())
                .build();
    }

}
