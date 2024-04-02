package com.amway.product.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amway.product.requests.CreateProductRequest;
import com.amway.product.requests.UpdateProductRequest;
import com.amway.product.responses.CreateProductResponse;
import com.amway.product.responses.GetProductResponse;
import com.amway.product.responses.UpdateProductResponse;
import com.amway.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController("v1")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get Product API")
	public GetProductResponse getProduct(
			@RequestParam(name = "product_code", required = true) String productCode) {
		return productService.getProduct(productCode);
	}

	@PostMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Save Product API")
	public CreateProductResponse addProduct(@RequestBody @Valid CreateProductRequest productRequest) {
		return productService.createProduct(productRequest);
	}
	
	
	@PutMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Update Product API")
	public UpdateProductResponse updateProduct(@RequestBody @Valid UpdateProductRequest productRequest) {
		return productService.updateProduct(productRequest);
	}
}
