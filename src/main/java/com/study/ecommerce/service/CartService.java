package com.study.ecommerce.service;

import com.study.ecommerce.domain.Cart;
import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.Product;
import com.study.ecommerce.exception.NotFoundCartProductException;
import com.study.ecommerce.exception.NotFoundMemberException;
import com.study.ecommerce.exception.NotFoundProduct;
import com.study.ecommerce.repository.CartRepository;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.repository.ProductRepository;
import com.study.ecommerce.request.CartRequest;
import com.study.ecommerce.response.CartListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;


    @Transactional(readOnly = true)
    public List<CartListResponse> getCartList(){
        String email = getMemberEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);

        List<Cart> listCart= cartRepository.findAllByMember(member);

        return listCart.stream().map(CartListResponse::from).toList();
    }

    @Transactional
    public void addCartProduct(CartRequest cartRequest){
        String email = getMemberEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);

        Product product = productRepository.findById(cartRequest.getProductId()).orElseThrow(NotFoundProduct::new);

        Optional<Cart> memberCart = cartRepository.findByMemberAndCartProduct(member,product);


        if (memberCart.isEmpty()){
            Cart cart = Cart.createCart(member,product,cartRequest.getQuantity());

            cartRepository.save(cart);

        }else{

            memberCart.get().addQuantity(cartRequest.getQuantity());

        }
    }

    @Transactional
    public void removeCartProduct(CartRequest cartRequest){
        String email = getMemberEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);

        Product product = productRepository.findById(cartRequest.getProductId()).orElseThrow(NotFoundProduct::new);

        Optional<Cart> memberCart = cartRepository.findByMemberAndCartProduct(member,product);

        if (memberCart.isPresent()){
            cartRepository.delete(memberCart.get());
        }else{
            throw new NotFoundCartProductException();
        }

    }

    @Transactional
    public void modifyCartProduct(CartRequest cartRequest){
        String email = getMemberEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);

        Product product = productRepository.findById(cartRequest.getProductId()).orElseThrow(NotFoundProduct::new);

        Optional<Cart> memberCart = cartRepository.findByMemberAndCartProduct(member,product);

        if (memberCart.isPresent()){
            memberCart.get().setQuantity(cartRequest.getQuantity());
        }else{
            throw new NotFoundCartProductException();
        }
    }



    public String getMemberEmail(){
       return SecurityContextHolder.getContext().getAuthentication().getName();
    }










}
