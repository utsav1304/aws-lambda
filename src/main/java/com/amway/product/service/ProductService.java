package com.amway.product.service;

import com.amway.product.requests.CreateProductRequest;
import com.amway.product.requests.UpdateProductRequest;
import com.amway.product.responses.CreateProductResponse;
import com.amway.product.responses.GetProductResponse;
import com.amway.product.responses.UpdateProductResponse;

import reactor.core.publisher.Mono;

public interface ProductService {

	Mono<GetProductResponse> getProduct(String productCode);
	
	Mono<CreateProductResponse> createProduct(CreateProductRequest createProduct);

	Mono<UpdateProductResponse> updateProduct(UpdateProductRequest productRequest);
}
