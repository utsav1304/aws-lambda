package com.am.product.service;

import com.am.product.requests.CreateProductRequest;
import com.am.product.requests.UpdateProductRequest;
import com.am.product.responses.CreateProductResponse;
import com.am.product.responses.GetProductResponse;
import com.am.product.responses.UpdateProductResponse;


public interface ProductService {

	GetProductResponse getProduct(String productCode);
	
	CreateProductResponse createProduct(CreateProductRequest createProduct);

	UpdateProductResponse updateProduct(UpdateProductRequest productRequest);
}
