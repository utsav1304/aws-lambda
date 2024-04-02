package com.amway.product.repostories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amway.product.repostories.model.Product;

@Repository
public interface ProductRepo extends CrudRepository<Product, String> {
	
	
	@Query("SELECT * FROM product WHERE product_code = :productCode")
	Product findByProductCode(@Param("productCode") String productCode);

}
