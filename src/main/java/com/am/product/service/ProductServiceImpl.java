package com.am.product.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.am.product.exception.ProductNotFoundException;
import com.am.product.repostories.ProductRepo;
import com.am.product.repostories.model.Product;
import com.am.product.requests.CreateProductRequest;
import com.am.product.requests.UpdateProductRequest;
import com.am.product.responses.CreateProductResponse;
import com.am.product.responses.GetProductResponse;
import com.am.product.responses.UpdateProductResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepo productRepo;

	@Override
	public GetProductResponse getProduct(String productCode) {
		log.debug(productCode);
		var productResponse = productRepo.findByProductCode(productCode);
		if(productResponse.isEmpty()) {
			throw new ProductNotFoundException(productCode);
		}

		return mapGetResponse(productResponse.get());
	}

	@Override
	public CreateProductResponse createProduct(CreateProductRequest createProduct) {
 
		var savedProduct = productRepo.putProduct(mapProduct(createProduct));
		return generateCreateResponse(savedProduct);
				
	}

	/**
	 * @param savedProduct
	 * @return
	 */
	private CreateProductResponse generateCreateResponse(Product savedProduct) {
		var createResponse = CreateProductResponse.builder();
		createResponse.status("Success").productCode(savedProduct.getProductCode());
		return createResponse.build();
	}

	@Override
	public UpdateProductResponse updateProduct(UpdateProductRequest productRequest) {
		var productCode = productRequest.getProductCode();
		var existingProduct = productRepo.findByProductCode(productCode);
		if (existingProduct.isEmpty()) {
			throw new ProductNotFoundException(productCode);
		}

		var updatedProduct = mapUpdateProduct(existingProduct.get(), productRequest);
		var updatedProductResponse = productRepo.putProduct(updatedProduct);

		return buildUpdateResponse(updatedProductResponse);
	}

	private UpdateProductResponse buildUpdateResponse(Product savedProduct) {
		var updateResponse = UpdateProductResponse.builder();
		updateResponse.status("Success").productCode(savedProduct.getProductCode());
		return updateResponse.build();
	}

	private Product mapUpdateProduct(Product product, UpdateProductRequest productRequest) {

		if (Objects.nonNull(productRequest.getPrice())) {
			product.setPrice(productRequest.getPrice());
		}

		if (Objects.nonNull(productRequest.getImagUrl())) {
			product.setImagUrl(productRequest.getImagUrl());

		}

		if (Objects.nonNull(productRequest.getIsSellable())) {
			product.setIsSellable(productRequest.getIsSellable());
		}

		if (Objects.nonNull(productRequest.getProductName())) {
			product.setProductName(productRequest.getProductName());
		}

		if (Objects.nonNull(productRequest.getProductDescription())) {
			product.setProductDescription(productRequest.getProductDescription());
		}
		//product.setNewProduct(false);

		return product;
	}

	private Product mapProduct(CreateProductRequest productRequest) {
		var product = Product.builder();

		var isSelleable = Objects.nonNull(productRequest.getIsSellable()) ? productRequest.getIsSellable()
				: Boolean.TRUE;

		product.price(productRequest.getPrice()).imagUrl(productRequest.getImagUrl()).isSellable(isSelleable);
		product.productName(productRequest.getProductName()).productDescription(productRequest.getProductDescription());
		product.productCode(productRequest.getProductCode());

		return product.build();
	}

	private GetProductResponse mapGetResponse(Product prod) {
		var getResponse = GetProductResponse.builder();

		getResponse.productCode(prod.getProductCode()).productName(prod.getProductName())
				.productDescription(prod.getProductDescription());
		getResponse.price(prod.getPrice()).imagUrl(prod.getImagUrl()).isSellable(prod.getIsSellable());
		return getResponse.build();
	}

}
