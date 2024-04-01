package com.amway.product.repostories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.amway.product.repostories.model.Product;

import reactor.core.publisher.Mono;

@Repository
public interface ProductRepo extends ReactiveCrudRepository<Product, String> {
	
	
	@Query("SELECT * FROM product WHERE product_code = :productCode")
	Mono<Product> findByProductCode(@Param("productCode") String productCode);

}
