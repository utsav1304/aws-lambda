package com.amway.product.service;

import com.amway.product.requests.CreateProductRequest;
import com.amway.product.requests.UpdateProductRequest;
import com.amway.product.responses.CreateProductResponse;
import com.amway.product.responses.GetProductResponse;
import com.amway.product.responses.UpdateProductResponse;


public interface ProductService {

	GetProductResponse getProduct(String productCode);
	
	CreateProductResponse createProduct(CreateProductRequest createProduct);

	UpdateProductResponse updateProduct(UpdateProductRequest productRequest);
}
