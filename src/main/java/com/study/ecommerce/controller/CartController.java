package com.study.ecommerce.controller;

import com.study.ecommerce.request.CartRequest;
import com.study.ecommerce.response.CartListResponse;
import com.study.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @GetMapping("/cart")
    public ResponseEntity<List<CartListResponse>> getCartList(){
        List<CartListResponse> cartListResponses = cartService.getCartList();

        return ResponseEntity.ok(cartListResponses);
    }


    @PostMapping("/cart")
    public ResponseEntity<Void> addCartProduct(@Valid @RequestBody CartRequest cartRequest){

        cartService.addCartProduct(cartRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/cart")
    public ResponseEntity<Void> modifyCartProduct(@Valid @RequestBody CartRequest cartRequest){

        cartService.modifyCartProduct(cartRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/cart")
    public ResponseEntity<Void> removeCartProduct(@Valid @RequestBody CartRequest cartRequest){

        cartService.removeCartProduct(cartRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
