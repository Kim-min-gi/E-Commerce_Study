package com.study.ecommerce.repository;

import com.study.ecommerce.domain.OrderProduct;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct,Long> {

    List<OrderProduct> findByOrderId(Long orderId);

}
