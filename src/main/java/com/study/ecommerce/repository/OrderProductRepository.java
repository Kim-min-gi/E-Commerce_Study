package com.study.ecommerce.repository;

import com.study.ecommerce.domain.OrderProduct;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct,Long> {


}
